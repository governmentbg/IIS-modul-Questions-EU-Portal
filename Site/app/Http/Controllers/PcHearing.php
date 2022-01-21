<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PcHearing extends Controller
{

    public function fnHearing(Request $request)
    {
        // return $type;
        // return $request->L_Ses_id["id"];
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


        $funnel = DB::table('PK_IZSLUSHVANE as q')
            ->leftjoin('PK_ADRESATI_IZSLUSHVANE as ai', 'ai.ID_IZSLUSHVANE', '=', 'q.ID')
            ->leftjoin('A_ns_Mps_Lng as mpl', function ($join) use ($lng) {
                $join->on('mpl.A_ns_MP_id', '=', 'ai.A_ns_MP_id')
                    ->where('mpl.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_MShip as ms', 'ms.A_ns_MP_id', '=', 'mpl.A_ns_MP_id')
            ->leftjoin('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 'ms.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Type as t', 't.PK_VA_T_id', '=', 'q.VID')
            ->join('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'q.STATUS');

        $funnel->select(
            'q.ID',
            'q.ANOT',
            'q.RN_DOC',
            // 'q.VID_OTG',
            // 'q.DAT_PISOTG',
            'q.DATE_DOC',
            'pl.A_ns_MP_PosL_value',
            'mpl.A_ns_MPL_Name1',
            's.PK_Va_S_name'



        );


        if ($A_ns_id) {
            $funnel->where('q.DATE_DOC', '>=', $A_ns_Assembly->A_ns_start)
                ->where('q.DATE_DOC', '<=', $A_ns_Assembly->A_ns_end);
        }

        if ($Pr_id) {
            if ($Pr_id == 'week') {
                // return 100;
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
            $funnel->join('PK_VNOSITELI_IZSLUSHVANE as v', 'v.ID_IZSLUSHVANE', '=', 'q.ID')
                ->where('v.A_ns_MP_id', '=', $A_ns_MP_id);
        }



        // $funnel->where('q.DATE_DOC', '>', '2013-05-20');


        $funnel->orderBy('q.DATE_DOC', 'desc');
        if ($request->search) {
            $funnel->take(300);
        } else {

            $funnel->take(30);
        }
        $funnel = $funnel->get();


        foreach ($funnel as $key_parent1 => $fn) {
            $imp_list = DB::table('PK_VNOSITELI_IZSLUSHVANE as v')
                ->join('A_ns_Mps_Lng as mpl_1', function ($join) use ($lng) {
                    $join->on('mpl_1.A_ns_MP_id', '=', 'v.A_ns_MP_id')
                        ->where('mpl_1.C_Lang_id', '=',  $lng);
                })
                ->where('v.ID_IZSLUSHVANE', $fn->ID)
                ->select('mpl_1.A_ns_MP_id', 'mpl_1.A_ns_MPL_Name1', 'mpl_1.A_ns_MPL_Name2', 'mpl_1.A_ns_MPL_Name3')
                ->get();



            $funnel[$key_parent1]->imp_list = $imp_list;


            // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
        }








        $response = Response::json($funnel, 200);
        return $response;
    }


    public function hearingProfile($id)
    {

        $id = (int)$id;
        $lng = 1;



        $hearing = DB::table('PK_IZSLUSHVANE as q')
            ->leftjoin('PK_ADRESATI_IZSLUSHVANE as ai', 'ai.ID_IZSLUSHVANE', '=', 'q.ID')
            ->leftjoin('A_ns_Mps_Lng as mpl', function ($join) use ($lng) {
                $join->on('mpl.A_ns_MP_id', '=', 'ai.A_ns_MP_id')
                    ->where('mpl.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_MShip as ms', 'ms.A_ns_MP_id', '=', 'mpl.A_ns_MP_id')
            ->leftjoin('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 'ms.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->join('PK_VAPROS_Type as t', 't.PK_VA_T_id', '=', 'q.VID')
            ->join('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'q.STATUS')
            ->leftjoin('Pl_StenV as st', 'st.Pl_Sten_date', '=', 'q.DAT_IZSLUSHVANE')

            ->select(
                'q.ID',
                'q.ANOT',
                'q.RN_DOC',
                'q.DAT_IZSLUSHVANE',
                'q.STATUS',
                'q.DATE_DOC',
                'pl.A_ns_MP_PosL_value',
                'mpl.A_ns_MPL_Name1',
                's.PK_Va_S_name',
                'st.Pl_Sten_id',
                'st.A_ns_id',

            )


            ->where('q.ID', $id)
            ->first();

        if ($hearing) {


            $hearing->imp_list = DB::table('PK_VNOSITELI_IZSLUSHVANE as v')
                // ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.VNOSITEL')
                ->join('A_ns_Mps_Lng as mpl_2', function ($join) {
                    $join->on('mpl_2.A_ns_MP_id', '=', 'v.A_ns_MP_id')
                        ->where('mpl_2.C_Lang_id', '=',  1);
                })
                ->where('v.ID_IZSLUSHVANE', $hearing->ID)
                // ->orderBy('v.ID', 'asc')
                ->select('mpl_2.A_ns_MP_id', 'mpl_2.A_ns_MPL_Name1', 'mpl_2.A_ns_MPL_Name2', 'mpl_2.A_ns_MPL_Name3')
                ->get();
        }
        $response = Response::json($hearing, 200);


        return $response;
    }
}
