<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class ParlRegisterController extends Controller
{
    public function strList()
    {
        // return 1;




        $contactList = [

            'com' => $this->colList(3),
            'groups' => $this->colList(2),
            'mp' => $this->mpList(),

        ];

        $response = Response::json($contactList, 200);
        return $response;
    }


    public function colList($type)
    {
        // return 1;

        $lng =  1;


        $colList = DB::table('A_ns_Satr as s')
            ->join('A_ns_Coll as c', 'c.A_ns_C_id', '=', 's.ns_Satr_SID')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'c.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            });



        if ($type == 3) {
            $colList->where(function ($query) use ($type) {
                $query->where('c.A_ns_CT_id', $type)
                    ->orWhere('c.A_ns_CT_id', 4);
            });
            // $colList->where('cl.A_ns_CT_id', 4);
        } else {
            $colList->where('c.A_ns_CT_id', $type);
        }

        $colList->where('s.ns_SatrT_id', $type)
            ->where('s.ns_Satr_date_to', '>=', Carbon::now())
            ->where('c.A_ns_id', currentNS())
            ->select(
                'c.A_ns_C_id',
                'c.A_ns_CT_id',
                'c18n.A_ns_CL_value',
                's.ns_Satr_id',
                's.ns_Satr_name',
                's.ns_Satr_edu',
                's.ns_Satr_area',


            )
            ->orderBy('c.A_ns_CT_id', 'asc')
            ->orderBy('c.A_ns_C_id', 'asc')
            ->orderBy('s.ns_Satr_name', 'asc');
        // 

        // $colList = $colList->get();
        return $colList->get();

        // $response = Response::json($colList, 200);
        // return $response;
    }


    public function mpList()
    {
        // return 1;

        $lng = 1;
        $colId = currentNSCID();


        $colListMP = DB::table('A_ns_Satr as st')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 'st.ns_Satr_SID')
            ->join('A_ns_MShip as s', 's.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            ->join('A_ns_MP_Pos as p', 'p.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            ->join('A_ns_Mps_Lng as mpl_10', function ($join) use ($lng) {
                $join->on('mpl_10.A_ns_MP_id', '=', 's.A_ns_MP_id')
                    ->where('mpl_10.C_Lang_id', '=',  $lng);
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
            ->where('cl.A_ns_CT_id', 2)
            ->where('st.ns_SatrT_id', 1)
            ->where('st.ns_Satr_date_to', '>=', Carbon::now());

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

            'mpl_10.A_ns_MPL_Name1',
            'mpl_10.A_ns_MPL_Name2',
            'mpl_10.A_ns_MPL_Name3',
            'pl.A_ns_MP_PosL_value',
            'va.A_ns_Va_name',
            'st.ns_Satr_id',
            'st.ns_Satr_name',
            'st.ns_Satr_edu',
            'st.ns_Satr_area',

        )->orderBy('mpl_10.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_10.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_10.A_ns_MPL_Name3', 'asc');

        // 

        // $colList = $colList->get();
        return $colListMP->get();

        // $response = Response::json($colList, 200);
        // return $response;
    }
}
