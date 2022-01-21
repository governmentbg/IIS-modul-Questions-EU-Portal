<?php

namespace App\Http\Controllers;


use Illuminate\Http\Request;
use \Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

use Carbon\Carbon;

class ArchiveController extends Controller
{

    

    public function collCount($field, $date, $colId)
    {



        $A_ns_C_active_count = DB::table('A_ns_MShip')
            ->where('A_ns_C_id', $colId);
        if ($date) {
            $A_ns_C_active_count->where('A_ns_MSP_date_T', '>=', $date)
                ->where('A_ns_MSP_date_F', '<=', $date);
        }


        $A_ns_C_active_count =  $A_ns_C_active_count->distinct()->count('A_ns_MP_id');

        echo $A_ns_C_active_count . "<br>";

        DB::table('A_ns_Coll')->where('A_ns_C_id', $colId)
            ->update([$field => $A_ns_C_active_count]);

        return $A_ns_C_active_count;
    }


    public function archive($lng)
    {
        $lng =  convert18n($lng);
        $archive = DB::table('A_ns_Assembly as a')
            ->join('A_ns_Assembly_Lng as a18n', function ($join) use ($lng) {
                $join->on('a18n.A_ns_id', '=', 'a.A_ns_id')
                    ->where('a18n.C_Lang_id', '=',  $lng);
            })

            ->where('a.C_Arch_id', 1)
            ->whereNull('a.deleted_at')
            ->select(
                'a.A_ns_id',
                'a.A_ns_start',
                'a.A_ns_end',
                'a18n.A_nsL_value',
            )
            ->orderBy('a.A_ns_start', 'desc')
            ->get();

        foreach ($archive as $key_parent1 => $list) {

            $pg = $this->colTCount(2, $list->A_ns_id);
            $cm = $this->colTCount(3, $list->A_ns_id);
            $cm_v = $this->colTCount(4, $list->A_ns_id);
            $dl = $this->colTCount(6, $list->A_ns_id);
            $cm_u = $this->colTCount(9, $list->A_ns_id);
            $fg = $this->colTCount(11, $list->A_ns_id);
            $pc = $this->colTCount(14, $list->A_ns_id);

            $archive[$key_parent1]->pg = $pg;
            $archive[$key_parent1]->cm = $cm;
            $archive[$key_parent1]->cm_v = $cm_v;
            $archive[$key_parent1]->dl = $dl;
            $archive[$key_parent1]->cm_u = $cm_u;
            $archive[$key_parent1]->fg = $fg;
            $archive[$key_parent1]->pc = $pc;
        }



        $response = Response::json($archive, 200);
        return $response;
    }

    public function colTCount($colTId, $nsId)
    {

        return DB::table('A_ns_Coll')
            ->where('A_ns_CT_id', $colTId)
            ->where('A_ns_id', $nsId)
            ->whereNull('A_ns_C_exclude')
            ->count();
    }

    public function archiveProfile($lng, $id)
    {
        $lng =  convert18n($lng);
        $archiveProfile = DB::table('A_ns_Assembly as a')
            ->join('A_ns_Assembly_Lng as a18n', function ($join) use ($lng) {
                $join->on('a18n.A_ns_id', '=', 'a.A_ns_id')
                    ->where('a18n.C_Lang_id', '=',  $lng);
            })

            ->where('a.C_Arch_id', 1)
            ->where('a.A_ns_id', $id)
            ->whereNull('a.deleted_at')
            ->select(
                'a.A_ns_id',
                'a.A_ns_start',
                'a.A_ns_end',
                'a18n.A_nsL_value',
                'a18n.A_nsL_body',
            )
            ->orderBy('a.A_ns_start', 'desc')
            ->first();

        $archiveProfile->pg = $this->colData($lng, 2, $id);
        $archiveProfile->cm = $this->colData($lng, 3, $id);
        $archiveProfile->cm_v = $this->colData($lng, 4, $id);
        $archiveProfile->cm_u = $this->colData($lng, 9, $id);
        $archiveProfile->dl = $this->colData($lng, 6, $id);
        $archiveProfile->fg = $this->colData($lng, 11, $id);
        $archiveProfile->pc = $this->colData($lng, 14, $id);





        $response = Response::json($archiveProfile, 200);
        return $response;
    }


    public function colData($lng, $ctId, $nsId)
    {
        // $lng =  convert18n($lng);
        return  DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->select(
                'cl.A_ns_C_id',
                'cl.A_ns_C_name',
                'cl.A_ns_C_date_F',
                'cl.A_ns_C_date_T',
                'cl.A_ns_C_start_count',
                'cl.A_ns_C_end_count',
                'cl.A_ns_C_total_count',
                'c18n.A_ns_CL_value',
            )
            ->where('cl.A_ns_id', $nsId)
            ->where('cl.A_ns_CT_id', $ctId)
            ->get();
    }
}
