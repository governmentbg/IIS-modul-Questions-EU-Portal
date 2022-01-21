<?php


namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PcBlicController extends Controller
{

    public function fnBlic(Request $request)
    {
        // return $type;
        // return $request->L_Ses_id["id"];
        $lng = 1;
        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }

        // if (isset($request->A_ns_MP_id)) {
        //     $A_ns_MP_id =  (int) $request->A_ns_MP_id["id"];
        // } else {
        //     $A_ns_MP_id =  null;
        // }
        // if (isset($request->A_ns_MP_min_id)) {
        //     $A_ns_MP_min_id =  (int) $request->A_ns_MP_min_id["id"];
        // } else {
        //     $A_ns_MP_min_id =  null;
        // }
        // // $A_ns_id = (int)$request->A_ns_id["id"];
        // // $A_ns_MP_id = (int)$request->A_ns_MP_id["id"];
        // // $A_ns_MP_min_id = (int)$request->A_ns_MP_min_id["id"];
        // $fnString = cleanUp($request->fnString);
        // $RN_DOC = cleanUp($request->RN_DOC);

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


        $funnel = DB::table('PK_BLIC as v');

        $funnel->select(
            // 'q.ID',
            // 'q.ANOT',
            // 'q.RN_DOC',
            // // 'q.VID_OTG',
            // // 'q.DAT_PISOTG',
            // 'v.DAT_BLIC',
            // 'pl.A_ns_MP_PosL_value',
            // 'mpl.A_ns_MPL_Name1',
            // 's.PK_Va_S_name'



        );


        if ($A_ns_id) {
            $funnel->where('v.DAT_BLIC', '>=', $A_ns_Assembly->A_ns_start)
                ->where('v.DAT_BLIC', '<=', $A_ns_Assembly->A_ns_end);
        }

        if ($Pr_id) {
            if ($Pr_id == 'week') {
                // return 100;
                $funnel->whereBetween('v.DAT_BLIC', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('v.DAT_BLIC', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('v.DAT_BLIC', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {

                $A_ns_Assembly = DB::table('A_ns_Assembly as cl')
                    ->where('A_ns_id', currentNS())

                    ->select(
                        'A_ns_start',
                        'A_ns_end',

                    )
                    ->first();

                $funnel->where('v.DAT_BLIC', '>=', $A_ns_Assembly->A_ns_start)
                    ->where('v.DAT_BLIC', '<=', $A_ns_Assembly->A_ns_end);
            }
        }







        $funnel->orderBy('v.DAT_BLIC', 'desc');

        $funnel = $funnel->get();



        $response = Response::json($funnel, 200);
        return $response;
    }


    public function blicProfile($id)
    {

        $id = (int)$id;

        $dateBlic = DB::table('PK_BLIC as v')->select('DAT_BLIC')
            ->where('v.ID', $id)
            ->first();

        // return $dateBlic->DAT_BLIC;
        // dd($dateBlic->DAT_BLIC);

        $lng = 1;

        if (!$dateBlic) {

            return Response::json([], 200);
        }

        $profile = DB::table('PK_VAPROS_BLIC as v')

            ->join('PK_Clob as c', function ($join) {
                $join->on('c.ID_OBJECT', '=', 'v.ID')
                    ->where('c.CODE_OBJECT', 103)
                    ->where('c.CODE_ATTRIBUTE', 5);
            })



            ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.ADRESAT')



            ->join('A_ns_Mps_Lng as mpl', function ($join) use ($lng) {
                $join->on('mpl.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MShip as ms', 'ms.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 'ms.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })

            ->select(
                'v.*',
                'c.*',
                'mpl.A_ns_MPL_Name1',
                'pl.*',
                'v.ID as ID_VAPROS',


            )


            ->where('v.DAT_VAPROS', $dateBlic->DAT_BLIC)
            ->orderBy('v.POR_NOM', 'asc')
            ->groupBy('v.ID')
            ->get();

        if ($profile) {

            foreach ($profile as $key_parent1 => $fn) {
                $imp_list = DB::table('PK_VNOSITELI_BLIC as v')
                    ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.VNOSITEL')
                    ->join('A_ns_Mps_Lng as mpl6', function ($join) use ($lng) {
                        $join->on('mpl6.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                            ->where('mpl6.C_Lang_id', '=',  $lng);
                    })
                    ->where('v.ID_VAPROS', $fn->ID_VAPROS)
                    ->select('mpl6.A_ns_MP_id', 'mpl6.A_ns_MPL_Name1', 'mpl6.A_ns_MPL_Name2', 'mpl6.A_ns_MPL_Name3')
                    ->groupBy('v.VNOSITEL')
                    ->get();



                $profile[$key_parent1]->imp_list = $imp_list;


                // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
            }
        }
        $response = Response::json($profile, 200);


        return $response;
    }
}
