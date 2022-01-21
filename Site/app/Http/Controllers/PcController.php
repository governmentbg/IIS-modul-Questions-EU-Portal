<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PcController extends Controller
{
    public function controlProgram($id = 0)
    {
        // return 1;
        $id = (int)$id;

        if (!$id) {
            $id = DB::table('Pl_Control as t1')
                ->join('Pl_Control_info as t2', 't2.DAT_PLAN', '=', 't1.Pl_Control_date')

                ->where('t1.C_St_id', 1)
                ->select('t1.Pl_Control_id')

                ->where('t1.Pl_Control_date', '<', Carbon::now()->endOfWeek())
                ->orderBy('t1.Pl_Control_id', 'desc')
                ->first()->Pl_Control_id;
        }
        $control  = [];
        if ($id) {



            $control = DB::table('Pl_Control as t1')
                ->leftjoin('Pl_Control_info as t2', 't2.DAT_PLAN', '=', 't1.Pl_Control_date')

                ->where('t1.C_St_id', 1)
                ->select(
                    't1.Pl_Control_id',
                    't1.Pl_Control_date',
                    't1.Pl_Control_body',
                    't1.Pl_Control_body1',
                    't1.Pl_Control_body2',
                    't1.Pl_Control_body3',
                    't2.TEKST_PREDI',
                    't2.TEKST_SLED'
                )

                ->where('t1.Pl_Control_id', $id)
                ->where('t1.C_St_id', 1);
            $control = $control->first();
            $control->TEKST_PREDI = nl2br($control->TEKST_PREDI);
            $control->TEKST_SLED = nl2br($control->TEKST_SLED);
            $control->Pl_Control_body = nl2br($control->Pl_Control_body);
            $control->Pl_Control_body1 = nl2br($control->Pl_Control_body1);
            $control->Pl_Control_body2 = nl2br($control->Pl_Control_body2);
            $control->Pl_Control_body3 = nl2br($control->Pl_Control_body3);
        }


        $response = Response::json($control, 200);


        return $response;
    }


    public function voteNocList()
    {
        $vote = DB::table('PK_VOT_NEDOVERIE as v')
            ->leftjoin('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'v.SASTOIANIE')
            ->join('PK_Clob as c', 'c.ID_OBJECT', '=', 'v.ID')

            ->where('c.CODE_OBJECT', 66)
            ->where('c.CODE_ATTRIBUTE', 5)
            ->select(
                // 'v.DAT_GLASUVANE',
                'v.ID',
                'v.DATE_DOC',
                'v.DAT_GLASUVANE',
                's.PK_Va_S_name',
                'c.TEKST',

            )


            ->orderBy('v.DATE_DOC', 'desc')
            ->orderBy('v.DAT_GLASUVANE', 'desc')
            ->get();
        $response = Response::json($vote, 200);


        return $response;
    }

    public function voteForList()
    {
        $vote = DB::table('PK_VOT_DOVERIE as v')
            ->join('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'v.RESHENIE')
            ->join('PK_Clob as c', 'c.ID_OBJECT', '=', 'v.ID')

            ->where('c.CODE_OBJECT', 65)
            ->where('c.CODE_ATTRIBUTE', 5)
            ->select(
                'v.ID',
                'v.DATE_DOC',
                'v.DAT_ZASEDANIE',
                's.PK_Va_S_name',
                'c.TEKST',

            )


            // ->orderBy('v.DATE_DOC', 'desc')
            ->orderBy('v.DAT_ZASEDANIE', 'desc')
            ->get();
        $response = Response::json($vote, 200);


        return $response;
    }


    public function voteNocProfile($id)
    {
        $id = (int)$id;
        $vote = DB::table('PK_VOT_NEDOVERIE as v')
            ->leftjoin('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'v.SASTOIANIE')
            ->join('PK_Clob as c', 'c.ID_OBJECT', '=', 'v.ID')
            ->leftjoin('Pl_StenV as st', 'st.Pl_Sten_date', '=', 'v.DAT_ZASEDANIE')
            ->where('c.CODE_OBJECT', 66)
            ->where('c.CODE_ATTRIBUTE', 5)
            ->select(
                'v.DATE_DOC',
                'v.RN_DOC',
                'v.ID',
                'v.DATE_DOC',
                'v.DAT_ZASEDANIE',
                'v.DAT_GLASUVANE',
                'v.REZULTDA',
                'v.REZULTNE',
                'v.REZULTVAZDARJAL',
                's.PK_Va_S_name',
                'c.TEKST',
                'st.A_ns_id',
                'st.Pl_Sten_id',

            )
            ->where('v.ID', $id)
            ->first();

        if ($vote) {



            $vote->imp_list = DB::table('PK_VNOSITELI_VOT as v')
                ->join('A_ns_Mps as mp', 'mp.sync_ID', '=', 'v.VNOSITEL')
                ->join('A_ns_Mps_Lng as mpl_10', function ($join) {
                    $join->on('mpl_10.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                        ->where('mpl_10.C_Lang_id', '=',  1);
                })
                ->where('v.ID_VOT', $id)
                ->groupBy('v.VNOSITEL')
                ->orderBy('v.ID', 'desc')
                ->select('v.VNOSITEL', 'mpl_10.A_ns_MP_id', 'mpl_10.A_ns_MPL_Name1', 'mpl_10.A_ns_MPL_Name2', 'mpl_10.A_ns_MPL_Name3')
                // ->groupBy('v.VNOSITEL')
                ->get();
        }

        // TODO find PDF with RN_DOC
        $response = Response::json($vote, 200);


        return $response;
    }



    public function voteForProfile($id)
    {
        $id = (int)$id;
        $vote = DB::table('PK_VOT_DOVERIE as v')
            ->join('PK_VAPROS_Status as s', 's.PK_Va_S_id', '=', 'v.RESHENIE')
            ->join('PK_Clob as c', 'c.ID_OBJECT', '=', 'v.ID')
            // ->leftjoin('L_Acts as l', 'l.L_Act_sign', '=', 'v.RN_DOC')
            ->leftjoin('L_Acts as l', function ($join) {
                $join->on('l.L_Act_sign', '=', 'v.RN_DOC')
                    ->where('l.L_Act_date2', '=', 'v.DAT_ZASEDANIE');
            })
            ->leftjoin('Pl_StenV as st', 'st.Pl_Sten_date', '=', 'v.DAT_ZASEDANIE')

            ->where('c.CODE_OBJECT', 65)
            ->where('c.CODE_ATTRIBUTE', 5)
            ->where('v.ID', $id)
            ->select(
                'v.ID',
                'v.DATE_DOC',
                'v.DAT_ZASEDANIE',
                'v.REZULTVAZDARJAL',
                'v.REZULTNE',
                'v.REZULTDA',
                'v.RN_DOC',
                's.PK_Va_S_name',
                'c.TEKST',
                'l.L_Act_id',
                'st.Pl_Sten_id',
                'st.A_ns_id',

            )



            ->first();
        $response = Response::json($vote, 200);


        return $response;
    }
}
