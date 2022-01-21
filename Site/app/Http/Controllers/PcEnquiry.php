<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PcEnquiry extends Controller
{

    public function fnEnquiry($type, Request $request)
    {

        // return Carbon::now()->startOfWeek();
        // 61 - актуален въпрос, 62 - питане
        if ($type == 62) {
            $type == 62;
        } else {
            $type == 61;
        }

        // return $type;
        // return $request;
        $lng = 1;
        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }

        if (isset($request->A_ns_MP_id)) {
            $A_ns_MP_id =  (int) $request->A_ns_MP_id["id"];
        } else {
            $A_ns_MP_id =  null;
        }
        if (isset($request->A_ns_MP_min_id)) {
            $A_ns_MP_min_id =  (int) $request->A_ns_MP_min_id["id"];
        } else {
            $A_ns_MP_min_id =  null;
        }


        // $A_ns_id = (int)$request->A_ns_id["id"];
        // $A_ns_MP_id = (int)$request->A_ns_MP_id["id"];
        // $A_ns_MP_min_id = (int)$request->A_ns_MP_min_id["id"];
        $fnString = cleanUp($request->fnString);
        $RN_DOC = cleanUp($request->RN_DOC);

        // if (!$A_ns_id) {
        //     $A_ns_id = currentNS();
        // }
        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
        }

        if (!$request->search) {
            $A_ns_id = currentNS();
        }
        if ($A_ns_id) {
            $A_ns_Assembly = DB::table('A_ns_Assembly as cl')
                ->where('A_ns_id', $A_ns_id)

                ->select(
                    'A_ns_start',
                    'A_ns_end',

                )
                ->first();
        }


        $funnel = DB::table('PK_VAPROS as q')
            ->join('PK_VAPROS_Type_Lng as tl1', function ($join) use ($lng) {
                $join->on('tl1.PK_VA_T_id', '=', 'q.Type')
                    ->where('tl1.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Type_Lng as tl2', function ($join) use ($lng) {
                $join->on('tl2.PK_VA_T_id', '=', 'q.VID_OTG')
                    ->where('tl2.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Mps_Lng as mpl', function ($join) use ($lng) {
                $join->on('mpl.A_ns_MP_id', '=', 'q.A_ns_MP_id')
                    ->where('mpl.C_Lang_id', '=',  $lng);
            })

            ->join('A_ns_MShip as s', 's.A_ns_MP_id', '=', 'mpl.A_ns_MP_id')
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Status_Lng as sl', function ($join) use ($lng) {
                $join->on('sl.PK_VA_S_id', '=', 'q.STATUS')
                    ->where('sl.C_Lang_id', '=',  $lng);
            });

        $funnel->select(
            'q.ID',
            'q.ANOT',
            'q.VID_OTG',
            'q.RN_DOC',
            'q.DAT_PISOTG',
            'q.DAT_OTG',
            'q.DATE_DOC',
            'sl.PK_VA_S_value',
            'pl.A_ns_MP_PosL_value',
            'mpl.A_ns_MPL_Name1',
            'tl1.PK_VA_TL_value AS TypeQuestion',
            'tl2.PK_VA_TL_value AS TypeAnswer'


        );


        if ($A_ns_id) {
            $funnel->where('q.DATE_DOC', '>=', $A_ns_Assembly->A_ns_start)
                ->where('q.DATE_DOC', '<=', $A_ns_Assembly->A_ns_end);
        }

        if ($Pr_id) {
            if ($Pr_id == 'week') {
                // return 'week';
                $funnel->whereBetween('q.DATE_DOC', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('q.DATE_DOC', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('q.DATE_DOC', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {

                $A_ns_Assembly = DB::table('A_ns_Assembly as cl')
                    ->where('A_ns_id', currentNS())

                    ->select(
                        'A_ns_start',
                        'A_ns_end',

                    )
                    ->first();

                $funnel->where('q.DATE_DOC', '>=', $A_ns_Assembly->A_ns_start)
                    ->where('q.DATE_DOC', '<=', $A_ns_Assembly->A_ns_end);
            }
        }

        if ($fnString) {
            $funnel->where('q.ANOT', 'like', '%' . $fnString . '%');
        }
        if ($RN_DOC) {
            $funnel->where('q.RN_DOC', 'like', '%' . $RN_DOC . '%');
        }
        if ($A_ns_MP_min_id) {
            $funnel->where('mpl.A_ns_MP_id', 'like', $A_ns_MP_min_id);
        }
        if ($A_ns_MP_id) {
            $funnel->join('PK_VNOSITELI as v', 'v.ID_VAPROS', '=', 'q.ID')
                ->where('v.A_ns_MP_id', '=', $A_ns_MP_id);
        }



        $funnel->where('q.Type', $type);
        // $funnel->where('q.DATE_DOC', '>', '2013-05-20')
        //     ->where('q.Type', $type);


        $funnel->orderBy('q.DATE_DOC', 'desc');
        if ($request->search) {
            $funnel->take(300);
        } else {

            $funnel->take(500);
        }
        $funnel = $funnel->get();


        foreach ($funnel as $key_parent1 => $fn) {
            $imp_list = DB::table('PK_VNOSITELI as v')
                ->join('A_ns_Mps_Lng as mpl8', function ($join) use ($lng) {
                    $join->on('mpl8.A_ns_MP_id', '=', 'v.A_ns_MP_id')
                        ->where('mpl8.C_Lang_id', '=',  $lng);
                })
                ->where('v.ID_VAPROS', $fn->ID)
                ->select('mpl8.A_ns_MP_id', 'mpl8.A_ns_MPL_Name1', 'mpl8.A_ns_MPL_Name2', 'mpl8.A_ns_MPL_Name3')
                ->get();



            $funnel[$key_parent1]->imp_list = $imp_list;


            // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
        }








        $response = Response::json($funnel, 200);
        return $response;
    }


    public function enquiryProfile($type, $id)
    {
        // 61 - актуален въпрос, 62 - питане
        if ($type == 62) {
            $type = 62;
        } else {
            $type = 61;
        }
        $id = (int)$id;
        $lng = 1;
        $enquiry = DB::table('PK_VAPROS as q')
            ->join('PK_VAPROS_Type_Lng as tl1', function ($join) use ($lng) {
                $join->on('tl1.PK_VA_T_id', '=', 'q.Type')
                    ->where('tl1.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Type_Lng as tl2', function ($join) use ($lng) {
                $join->on('tl2.PK_VA_T_id', '=', 'q.VID_OTG')
                    ->where('tl2.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Type as tl3', 'tl3.PK_VA_T_id', '=', 'q.VID_OTG')
            ->join('A_ns_Mps_Lng as mpl', function ($join) use ($lng) {
                $join->on('mpl.A_ns_MP_id', '=', 'q.A_ns_MP_id')
                    ->where('mpl.C_Lang_id', '=',  $lng);
            })

            ->join('A_ns_MShip as s', 's.A_ns_MP_id', '=', 'mpl.A_ns_MP_id')
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('Pl_StenV as st', 'st.Pl_Sten_date', '=', 'q.DAT_OTG')
            ->join('PK_VAPROS_Status_Lng as sl', function ($join) use ($lng) {
                $join->on('sl.PK_VA_S_id', '=', 'q.STATUS')
                    ->where('sl.C_Lang_id', '=',  $lng);
            })->select(
                'q.ID',
                'q.ANOT',
                'q.VID_OTG',
                'q.DAT_PISOTG',
                'q.DAT_OTG',
                'q.DAT_PLANOTG',
                'q.DATE_DOC',
                'q.RN_DOC',
                'q.DAT_OTLAG',
                'q.DAT_VRACHVANE',
                'pl.A_ns_MP_PosL_value',
                'mpl.A_ns_MPL_Name1',
                'tl3.PK_VA_T_name',
                'tl3.PK_VA_T_id',
                'tl1.PK_VA_TL_value AS TypeQuestion',
                'tl2.PK_VA_TL_value AS TypeAnswer',
                'sl.PK_VA_S_value',
                'sl.PK_VA_S_id',
                'st.Pl_Sten_id',
                'st.A_ns_id',


            )
            ->where('q.ID', $id)
            ->first();

        if ($enquiry) {


            $enquiry->imp_list = DB::table('PK_VNOSITELI as v')
                // ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.VNOSITEL')
                ->join('A_ns_Mps_Lng as mpl9', function ($join) {
                    $join->on('mpl9.A_ns_MP_id', '=', 'v.A_ns_MP_id')
                        ->where('mpl9.C_Lang_id', '=',  1);
                })
                ->where('v.ID_VAPROS', $enquiry->ID)
                // ->orderBy('v.ID', 'asc')
                ->select('mpl9.A_ns_MP_id', 'mpl9.A_ns_MPL_Name1', 'mpl9.A_ns_MPL_Name2', 'mpl9.A_ns_MPL_Name3')
                ->get();

            $enquiry->quest = DB::table('PK_Files')

                ->where('ID_OBJECT', $enquiry->ID)
                // ->where('CODE_OBJECT', $type)
                ->where('ROLIA', 1)
                ->where('C_St_id', 1)
                ->select('ID', 'TIP', 'FILENAME')
                ->get();

            $enquiry->answer = DB::table('PK_Files')

                ->where('ID_OBJECT', $enquiry->ID)
                // ->where('CODE_OBJECT', $type)
                ->where('ROLIA', 2)
                ->where('C_St_id', 1)
                ->select('ID', 'TIP', 'FILENAME')
                ->get();
        }
        $response = Response::json($enquiry, 200);


        return $response;
    }
}
