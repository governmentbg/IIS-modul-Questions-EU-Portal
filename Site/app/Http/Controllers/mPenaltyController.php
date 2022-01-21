<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class mPenaltyController extends Controller
{
    public function penList(Request $request)
    {
        // return $request;
        $lng =  1;
        if ($request->A_ns_id) {
            $colId = $request->A_ns_id["id"];
        } else {
            $colId = currentNS();
        }
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        $A_ns_MP_Name = cleanUp($request->A_ns_MP_Name);



        $list = DB::table('A_ns_MP_Pen as p')
            ->leftjoin('A_ns_MP_Pen_Type as t', 't.A_ns_MP_PenT_id', '=', 'p.A_ns_MP_PenT_id')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 'p.A_ns_MP_id')
            ->join('A_ns_Mps_Lng as mpl_8', function ($join) use ($lng) {
                $join->on('mpl_8.A_ns_MP_id', '=', 'p.A_ns_MP_id')
                    ->where('mpl_8.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Mps_Lng as mpl1', function ($join) use ($lng) {
                $join->on('mpl1.A_ns_MP_id', '=', 'p.A_ns_MP_Pen_befalf')
                    ->where('mpl1.C_Lang_id', '=',  $lng);
            })
            // ->join('A_ns_MShip as s', function ($join) {
            //     $join->on('s.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            //         ->where('s.A_ns_MP_Pos_id', '=', 3)
            //         ->orWhere('s.A_ns_MP_Pos_id', '=', 3)
            //     ;
            // })
            // ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
            //     $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            //         ->where('pl.C_Lang_id', '=',  $lng);
            // })

        ;
        if ($date1) {
            $list->where('p.A_ns_MP_Pen_date', '>=', $date1);
        }
        if ($date2) {
            $list->where('p.A_ns_MP_Pen_date', '<=', $date2);
        }

        if ($A_ns_MP_Name) {
            $list->where(function ($query) use ($A_ns_MP_Name) {
                $query->where('mpl_8.A_ns_MPL_Name1', 'like', "{$A_ns_MP_Name}%")
                    ->orWhere('mpl_8.A_ns_MPL_Name2', 'like', "{$A_ns_MP_Name}%")
                    ->orWhere('mpl_8.A_ns_MPL_Name3', 'like', "{$A_ns_MP_Name}%");
            });
        }



        $list->where('mp.A_ns_id', $colId)
            ->select(
                'p.A_ns_MP_Pen_id',
                'p.A_ns_MP_Pen_date',
                't.A_ns_MP_PenT_name',
                'p.A_ns_MP_Pen_note',
                'p.A_ns_MP_id',
                'mpl_8.A_ns_MPL_Name1',
                'mpl_8.A_ns_MPL_Name2',
                'mpl_8.A_ns_MPL_Name3',
                'p.A_ns_MP_Pen_befalf',
                'mpl1.A_ns_MPL_Name1 as A_ns_MP_Chr_Name1',
                'mpl1.A_ns_MPL_Name2 as A_ns_MP_Chr_Name2',
                'mpl1.A_ns_MPL_Name3 as A_ns_MP_Chr_Name3',
                // 'pl.A_ns_MP_PosL_value',

            )
            ->orderBy('p.A_ns_MP_Pen_date', 'desc');
        $list = $list->get();



        foreach ($list as $key_parent1 => $ls) {
            $activity = DB::table('A_ns_MP_Pen_Activity as a')
                ->join('A_ns_MP_Pen_Status as s', 's.A_ns_MP_PenS_id', '=', 'a.A_ns_MP_PenS_id')
                ->join('A_ns_MP_Pen_Type as t', 't.A_ns_MP_PenT_id', '=', 'a.A_ns_MP_PenT_id')

                ->where('a.A_ns_MP_Pen_id', $ls->A_ns_MP_Pen_id)
                // ->where('a.A_ns_MP_PenS_id', '!=', 1)
                ->select(
                    'a.A_ns_MP_PenAC_id',
                    's.A_ns_MP_PenS_name',
                )
                ->orderBy('a.A_ns_MP_PenAC_timestamp', 'asc')
                ->get();


            $list[$key_parent1]->activity = $activity;
        }


        $response = Response::json($list, 200);
        return $response;
    }
}
