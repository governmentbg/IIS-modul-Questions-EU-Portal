<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class ContactsController extends Controller
{

    public function contactList($lng)
    {
        // return 1;




        $contactList = [

            'com' => $this->colList($lng, 3),
            'groups' => $this->colList($lng, 2),
            'mp' => $this->mpList($lng),
            'adm' => $this->admList(),

        ];

        $response = Response::json($contactList, 200);
        return $response;
    }


    public function colList($lng, $type)
    {
        // return 1;

        $lng =  convert18n($lng);


        $colList = DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_ColData as d', function ($join) use ($lng) {
                $join->on('d.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('d.C_Lang_id', '=',  $lng);
            });

        if ($type == 3) {
            $colList->where(function ($query) use ($type) {
                $query->where('cl.A_ns_CT_id', $type)
                    ->orWhere('cl.A_ns_CT_id', 4);
            });
            // $colList->where('cl.A_ns_CT_id', 4);
        } else {
            $colList->where('cl.A_ns_CT_id', $type);
        }

        $colList->where('cl.A_ns_id', currentNS())
            ->select(
                'cl.A_ns_C_id',
                'cl.A_ns_CT_id',
                'c18n.A_ns_CL_value',
                // 'd.A_ns_CDphone',
                'd.A_ns_CDemail',


            )->orderBy('cl.A_ns_CT_id', 'asc')
            ->orderBy('cl.A_ns_C_id', 'asc');
        // 

        // $colList = $colList->get();
        return $colList->get();

        // $response = Response::json($colList, 200);
        // return $response;
    }


    public function mpList($lng)
    {
        // return 1;

        $lng =  convert18n($lng);
        $colId = currentNSCID();


        $colListMP = DB::table('A_ns_MShip as s')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 's.A_ns_MP_id')
            ->join('A_ns_MP_Pos as p', 'p.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            ->join('A_ns_Mps_Lng as mpl_4', function ($join) use ($lng) {
                $join->on('mpl_4.A_ns_MP_id', '=', 's.A_ns_MP_id')
                    ->where('mpl_4.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->where('s.A_ns_C_id', $colId)
            ->join('A_ns_MShip as s1', 's1.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 's1.A_ns_C_id')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 's1.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Va as va', 'va.A_ns_Va_id', '=', 'mp.A_ns_Va_id')
            ->where('s.A_ns_MSP_date_T', '>=', Carbon::now())
            ->where('s.A_ns_MSP_date_F', '<=', Carbon::now())
            ->where('s1.A_ns_MSP_date_T', '>=', Carbon::now())
            ->where('s1.A_ns_MSP_date_F', '<=', Carbon::now())
            ->where('cl.A_ns_CT_id', 2);

        $colListMP->select(
            'cl.A_ns_C_id',
            // 'cl.A_ns_C_active_cnt',
            // 'c18n.A_ns_CL_value',
            // 's.A_ns_MSP_date_F',
            // 's.A_ns_MSP_date_T',
            'mp.A_ns_MP_FM',
            'mp.A_ns_MP_id',
            'c18n.A_ns_CL_value',
            'c18n.A_ns_CL_value_short',
            // 'mp.A_ns_MP_fbook',
            'mp.A_ns_MP_Email',
            // 'mp.A_ns_MP_url',

            'mpl_4.A_ns_MPL_Name1',
            'mpl_4.A_ns_MPL_Name2',
            'mpl_4.A_ns_MPL_Name3',
            'pl.A_ns_MP_PosL_value',
            'va.A_ns_Va_name'

        )->orderBy('mpl_4.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_4.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_4.A_ns_MPL_Name3', 'asc');

        // 

        // $colList = $colList->get();
        return $colListMP->get();

        // $response = Response::json($colList, 200);
        // return $response;
    }


    public function admList()
    {
        // return 1;



        $admList = DB::table('Adm_Contacts as c')
            ->join('Adm_Dept as d', 'd.AdmD_id', '=', 'c.AdmD_id')
            ->join('Adm_Type as t', 't.AdmT_id', '=', 'd.AdmT_id')
            ->whereNull('t.deleted_at')
            ->whereNull('d.deleted_at')
            ->whereNull('c.deleted_at')


            ->select(
                'c.AdmC_id',
                'c.AdmC_name',
                'c.AdmC_email',
                'c.AdmC_phone',

                'd.AdmD_id',
                'd.AdmD_id',
                'd.AdmD_name',
                't.AdmT_id',
                't.AdmT_name',

            )->orderBy('d.AdmT_id', 'asc')
            ->orderBy('d.AdmD_id', 'asc')
            ->orderBy('c.AdmC_order', 'desc')
            ->orderBy('c.AdmC_name', 'asc');

        // 


        return $admList->get();
    }
}
