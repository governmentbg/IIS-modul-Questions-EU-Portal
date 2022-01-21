<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class FunnelController extends Controller
{
    public function fnAssembly($lng)
    {

        $lng =  convert18n($lng);


        $list = DB::table('A_ns_Assembly as cl')
            ->join('A_ns_Assembly_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_id', '=', 'cl.A_ns_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->whereYear('A_ns_start', '>', '2000')

            ->select(
                'cl.A_ns_id',
                'c18n.A_nsL_value',

            )
            ->orderBy('A_ns_start', 'desc')
            ->get();


        $response = Response::json($list, 200);
        return $response;
    }


    public function fnColl($lng, $type, $nsId = 0)
    {

        $lng =  convert18n($lng);
        if (!$nsId) {
            $nsId = currentNS();
        }


        $list =  DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->where('cl.A_ns_CT_id', $type)
            ->where('cl.C_St_id', 1)
            ->where('cl.A_ns_id', $nsId)
            ->select(
                'cl.A_ns_C_id',
                'cl.A_ns_CT_id',
                'c18n.A_ns_CL_value',
                'cl.A_ns_C_active_count',

            )
            ->orderBy('cl.A_ns_C_active_count', 'desc')
            ->orderBy('c18n.A_ns_CL_value', 'asc')
            ->get();


        $response = Response::json($list, 200);
        return $response;
    }


    public function fnSession($lng, $nsId = 0)
    {

        $lng =  convert18n($lng);
        if (!$nsId) {
            $nsId = currentNS();
        }


        $list =  DB::table('L_Sessions as cl')
            ->join('L_Sessions_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.L_Ses_id', '=', 'cl.L_Ses_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->where('cl.A_ns_id', $nsId)
            ->select(
                'cl.L_Ses_id',
                'cl.L_Ses_date_from',
                'cl.L_Ses_date_to',
                'c18n.L_SesL_value',

            )
            ->orderBy('cl.L_Ses_date_from', 'asc')
            ->get();


        $response = Response::json($list, 200);
        return $response;
    }

    public function fnMps($lng, $nsId = 0)
    {

        $lng =  convert18n($lng);
        if (!$nsId) {
            $nsId = currentNS();
        }
        // A_ns_Va_Type

        $list =  DB::table('A_ns_Mps as mp')
            ->join('A_ns_Mps_Lng as mpl_5', function ($join) use ($lng) {
                $join->on('mpl_5.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl_5.C_Lang_id', '=',  $lng);
            })

            ->where('mp.A_ns_id', $nsId)
            ->where('mp.A_ns_Va_Type', '!=', 3)
            ->select(

                'mp.A_ns_MP_id',
                DB::raw("CONCAT(mpl_5.A_ns_MPL_Name1,' ',mpl_5.A_ns_MPL_Name2, ' ',mpl_5.A_ns_MPL_Name3 ) as A_ns_MP_short_name")


            )
            ->orderBy('mpl_5.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_5.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_5.A_ns_MPL_Name3', 'asc')
            ->get();


        $response = Response::json($list, 200);
        return $response;
    }


    public function fnMin($lng, $nsId = 0)
    {

        $lng =  convert18n($lng);
        if (!$nsId) {
            $nsId = currentNS();
        }

        // fix for 45NS and no Min
        if ($nsId == 55) {
            $nsId = 52;
        }


        $list =  DB::table('A_ns_Mps as mp')
            ->join('A_ns_MShip as ms', 'ms.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            ->join('A_ns_Mps_Lng as mpl_5', function ($join) use ($lng) {
                $join->on('mpl_5.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl_5.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 'ms.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })

            ->where('mp.A_ns_id', $nsId)
            ->where('mp.A_ns_Va_Type', '=', 3)
            ->select(

                'mp.A_ns_MP_id',
                // 'mpl_5.A_ns_MPL_Name1',
                // 'pl.A_ns_MP_PosL_value',
                DB::raw("CONCAT(mpl_5.A_ns_MPL_Name1, ', ',pl.A_ns_MP_PosL_value)  as A_ns_MPL_Name1"),



            )
            // ->where('ms.A_ns_MSP_date_T', '>=', Carbon::now())
            ->orderBy('mpl_5.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_5.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_5.A_ns_MPL_Name3', 'asc')
            ->get();


        $response = Response::json($list, 200);
        return $response;
    }
}
