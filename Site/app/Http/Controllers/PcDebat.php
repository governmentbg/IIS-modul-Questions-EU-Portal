<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PcDebat extends Controller
{

    public function fnDebat(Request $request)
    {
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
        // $A_ns_id = (int)$request->A_ns_id["id"];
        // $A_ns_MP_id = (int)$request->A_ns_MP_id["id"];
        $fnString = cleanUp($request->fnString);
        $RN_DOC = cleanUp($request->RN_DOC);

        if (!$request->search) {
            $A_ns_id = currentNS();
        }
        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
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


        $funnel = DB::table('PK_RAZISKVANE as i')
            ->leftjoin('PK_VAPROS_Status as s', 's.PK_VA_S_id', '=', 'i.STATUS');

        $funnel->select(
            'i.DAT_PREDLOJENIE',
            'i.NOMER_PREDLOJENIE',
            'i.ID',
            'i.ANOT',
            's.PK_VA_S_name',


        );


        // if ($A_ns_id) {
        //     $funnel->where('i.DAT_PREDLOJENIE', '>=', $A_ns_Assembly->A_ns_start)
        //         ->where('i.DAT_PREDLOJENIE', '<=', $A_ns_Assembly->A_ns_end);
        // }

        if ($A_ns_id) {
            $funnel->where('i.DAT_PREDLOJENIE', '>=', $A_ns_Assembly->A_ns_start)
                ->where('i.DAT_PREDLOJENIE', '<=', $A_ns_Assembly->A_ns_end);
        }

        if ($Pr_id) {
            if ($Pr_id == 'week') {
                // return 100;
                $funnel->whereBetween('i.DAT_PREDLOJENIE', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('i.DAT_PREDLOJENIE', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('i.DAT_PREDLOJENIE', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {

                $A_ns_Assembly = DB::table('A_ns_Assembly as cl')
                    ->where('A_ns_id', currentNS())

                    ->select(
                        'A_ns_start',
                        'A_ns_end',

                    )
                    ->first();

                $funnel->where('i.DAT_PREDLOJENIE', '>=', $A_ns_Assembly->A_ns_start)
                    ->where('i.DAT_PREDLOJENIE', '<=', $A_ns_Assembly->A_ns_end);
            }
        }

        if ($fnString) {
            $funnel->where('i.ANOT', 'like', '%' . $fnString . '%');
        }
        if ($RN_DOC) {
            $funnel->where('i.NOMER_PREDLOJENIE', 'like', '%' . $RN_DOC . '%');
        }
        if ($A_ns_MP_id) {
            $funnel->join('PK_VNOSITELI_RAZISKVANE as vi', 'vi.ID_RAZISKVANE', '=', 'i.ID')
                ->where('vi.A_ns_MP_id', '=', $A_ns_MP_id);
        }


        $funnel->orderBy('i.DAT_PREDLOJENIE', 'desc');
        if ($request->search) {
            $funnel->take(300);
        } else {

            $funnel->take(30);
        }
        $funnel = $funnel->get();








        $response = Response::json($funnel, 200);
        return $response;
    }


    public function debatProfile($id)
    {
        $id = (int)$id;
        $debate = DB::table('PK_RAZISKVANE as i')
            ->leftjoin('PK_VAPROS as p', 'p.ID', '=', 'i.ID_PITANE')
            ->leftjoin('PK_VAPROS_Status as s', 's.PK_VA_S_id', '=', 'i.STATUS')

            ->select(
                'i.DAT_PREDLOJENIE',
                'i.ID',
                'i.ANOT',
                'p.DATE_DOC',
                'p.RN_DOC',
                'i.ID_PITANE',
                'i.NOMER_PREDLOJENIE',
                'i.DAT_NASRRAZ',
                'i.DAT_PROVRAZ',


            )
            ->where('i.ID', $id)
            ->first();

        if ($debate) {


            $debate->imp_list = DB::table('PK_VNOSITELI_RAZISKVANE as v')
                // ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.VNOSITEL')
                ->join('A_ns_Mps_Lng as mpl7', function ($join) {
                    $join->on('mpl7.A_ns_MP_id', '=', 'v.A_ns_MP_id')
                        ->where('mpl7.C_Lang_id', '=',  1);
                })
                ->where('v.ID_RAZISKVANE', $debate->ID)
                ->orderBy('v.ID', 'asc')
                ->select('mpl7.A_ns_MP_id', 'mpl7.A_ns_MPL_Name1', 'mpl7.A_ns_MPL_Name2', 'mpl7.A_ns_MPL_Name3')
                ->get();

            $debate->acts = DB::table('PK_RAZISKVANE_ACT as ra')
                ->join('L_Acts as a', 'a.Sync_ID', '=', 'ra.L_Act_SyncID')
                ->join('L_Acts_Lng as la', function ($join) {
                    $join->on('la.L_Act_id', '=', 'a.L_Act_id')
                        ->where('la.C_Lang_id', '=',  1);
                })
                ->join('L_Sessions_Lng as s1', function ($join) {
                    $join->on('s1.L_Ses_id', '=', 'a.L_Ses_id')
                        ->where('s1.C_Lang_id', '=',  1);
                })
                ->join('L_Sessions as s', 's.L_Ses_id', '=', 'a.L_Ses_id')
                ->join('A_ns_Assembly as s3', 's3.A_ns_id', '=', 's.A_ns_id')
                ->where('ra.ID_RAZISKVANE', $debate->ID)
                ->where('a.C_St_id', 1)

                ->select(
                    'a.L_Act_id',
                    'a.L_Act_sign',
                    'la.L_ActL_title',
                    's3.A_ns_folder',
                    'a.sync_ID',

                )->get();


            foreach ($debate->acts  as $key_parent1 => $fn) {


                $files = DB::table('L_Act_Files')

                    ->where('L_Act_id', $fn->sync_ID)
                    ->where('FILENAME', 'like', '%' . $fn->L_Act_sign . '%')
                    ->where('VID_DOC', 1550)
                    ->select('ID', 'TIP', 'FILENAME')
                    ->get();


                $debate->acts[$key_parent1]->files = $files;



                // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
            }
        }
        $response = Response::json($debate, 200);


        return $response;
    }
}
