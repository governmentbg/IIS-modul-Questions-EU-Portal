<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class mpAbsenseController extends Controller
{
    public function absList($lng, Request $request)
    {

        // return $request;
        $lng =  convert18n($lng);

        if ($request->A_ns_id) {
            $colId = $request->A_ns_id["id"];
        } else {
            $colId = currentNS();
        }
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        $A_ns_MP_Name = cleanUp($request->A_ns_MP_Name);


        $list = DB::table('MP_Absense as ab')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 'ab.A_ns_MP_id')
            ->join('A_ns_Mps_Lng as mpl_7', function ($join) use ($lng) {
                $join->on('mpl_7.A_ns_MP_id', '=', 'ab.A_ns_MP_id')
                    ->where('mpl_7.C_Lang_id', '=',  $lng);
            })
            ->join('MP_Absense_Type_Lng as t', function ($join) use ($lng) {
                $join->on('t.MP_Ab_T_id', '=', 'ab.MP_Ab_T_id')
                    ->where('t.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'ab.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            });



        if ($date1) {
            $list->where('ab.MP_Ab_date', '>=', $date1);
        }
        if ($date2) {
            $list->where('ab.MP_Ab_date', '<=', $date2);
        }

        if ($A_ns_MP_Name) {
            $list->where(function ($query) use ($A_ns_MP_Name) {
                $query->where('mpl_7.A_ns_MPL_Name1', 'like', "{$A_ns_MP_Name}%")
                    ->orWhere('mpl_7.A_ns_MPL_Name2', 'like', "{$A_ns_MP_Name}%")
                    ->orWhere('mpl_7.A_ns_MPL_Name3', 'like', "{$A_ns_MP_Name}%");
            });
        }



        $list->where('mp.A_ns_id', $colId)
            ->select(
                'ab.MP_Ab_id',
                'ab.MP_Ab_date',
                'ab.A_ns_MP_id',
                'ab.A_ns_C_id',
                'ab.MP_Ab_T_id',
                'mpl_7.A_ns_MPL_Name1',
                'mpl_7.A_ns_MPL_Name2',
                'mpl_7.A_ns_MPL_Name3',
                'c18n.A_ns_CL_value',


            )
            ->orderBy('ab.MP_Ab_date', 'desc');
        if ($request->search) {
            $list->take(1000);
        } else {

            $list->take(100);
        }
        $list = $list->get();






        $response = Response::json($list, 200);
        return $response;
    }
}
