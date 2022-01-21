<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;


class CivilContractController extends Controller
{
    public function contractList(Request $request)
    {

        $lng = 1;


        $contractList =  DB::table('PS_DOGOVORI as d')
            ->join('Adm_Sys_Classif as a1', 'a1.CODE', '=', 'd.PREDMET')
            ->join('Adm_Sys_Classif as a2', 'a2.CODE', '=', 'd.ETAP')

            ->leftjoin('A_ns_Mps as mp', 'mp.sync_ID', '=', 'd.VAZLOJITEL')
            ->leftjoin('A_ns_Mps_Lng as mpl_3', function ($join) use ($lng) {
                $join->on('mpl_3.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl_3.C_Lang_id', '=',  $lng);
            })

            ->leftjoin('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 'd.VAZLOJITEL')
            ->leftjoin('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })




            ->select(
                'd.ID',
                'd.IMENA',
                'd.VAZLOJITEL_TEXT',
                'd.TEMA',
                'd.DATA_OT',
                'd.DATA_DO',
                'd.SUMA',


                'a1.TEXT as T1',
                'a2.TEXT as T2',

                'cl.A_ns_C_id',
                'c18n.A_ns_CL_value',
                'c18n.A_ns_CL_value_short',

                'mp.A_ns_MP_id',
                'mp.A_ns_MP_FM',
                'mpl_3.A_ns_MPL_Name1',
                'mpl_3.A_ns_MPL_Name2',
                'mpl_3.A_ns__Name3',


            )
            ->where('d.PREDMET', '!=', '26122')
            ->orderBy('d.DATA_OT', 'desc');





        $contractList = $contractList->get();


        $response = Response::json($contractList, 200);
        return $response;
    }
}
