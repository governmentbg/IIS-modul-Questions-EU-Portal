<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class publicPrcController extends Controller
{
    public function opStatus()
    {

        $opStatus = DB::table('OP_Status')
            ->select('OP_S_id', 'OP_S_name')
            ->whereNull('deleted_at')
            ->get();

        $response = Response::json($opStatus, 200);
        return $response;
    }


    public function opProcType()
    {

        $opProcType = DB::table('OP_ProcType')
            ->select('OP_PrT_id', 'OP_PrT_name')
            ->whereNull('deleted_at')
            ->get();

        $response = Response::json($opProcType, 200);
        return $response;
    }


    public function opProfile($id)
    {

        $opProfile = DB::table('OP_Proc as t1')
            ->join('OP_ProcType as t2', 't2.OP_PrT_id', '=', 't1.OP_PrT_id')
            ->leftjoin('OP_ProcMType as t3', 't3.OP_PrMT_id', '=', 't1.OP_PrMT_id')
            ->join('OP_Status as t4', 't4.OP_S_id', '=', 't1.OP_S_id')
            ->select(
                't1.OP_Pr_id',
                't1.OP_Pr_date',
                't1.OP_Pr_title',
                't1.OP_Pr_aop_num',
                't1.OP_Pr_aop_an',
                't1.OP_Pr_body',
                't2.OP_PrT_name',
                't2.OP_PrT_id',
                't4.OP_S_id',
                't4.OP_S_name',
            )
            ->where('t1.OP_Pr_id', '=', $id)
            ->first();


        $opProfile->OP_Pr_body  = htmlspecialchars_decode($opProfile->OP_Pr_body, ENT_QUOTES);


        $opProfile->files = DB::table('OP_Articles')
            ->where('OP_Pr_id', $opProfile->OP_Pr_id)
            ->whereNull('deleted_at')

            ->select(
                'OP_A_id',
                'OP_A_date',
                'OP_A_title',
                'OP_A_body',
                DB::raw("IF(SUBSTR(OP_A_file, 1,4)='pub/',CONCAT('/',OP_A_file),CONCAT('/pub/publicprocurement/',OP_A_file))  as L_ActD_file"),

            )
            ->get();
        $response = Response::json($opProfile, 200);
        return $response;
    }



    public function fnProc(Request $request)
    {

        // return $request;

        if (isset($request->OP_S_id)) {
            $OP_S_id =  (int) $request->OP_S_id["id"];
        } else {
            $OP_S_id =  null;
        }


        if (isset($request->OP_PrT_id)) {
            $OP_PrT_id =  (int) $request->OP_PrT_id["id"];
        } else {
            $OP_PrT_id =  null;
        }



        $OP_Pr_title = cleanUp($request->OP_Pr_title);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);


        $funnel = DB::table('OP_Proc as t1')
            ->join('OP_ProcType as t2', 't2.OP_PrT_id', '=', 't1.OP_PrT_id')
            ->leftjoin('OP_ProcMType as t3', 't3.OP_PrMT_id', '=', 't1.OP_PrMT_id')
            ->join('OP_Status as t4', 't4.OP_S_id', '=', 't1.OP_S_id')
            ->select(
                't1.OP_Pr_id',
                't1.OP_Pr_date',
                't1.OP_Pr_title',
                't1.OP_Pr_aop_num',
                't1.OP_Pr_aop_an',
                // 't1.OP_Pr_body',
                't2.OP_PrT_name',
                't2.OP_PrT_id',
                't4.OP_S_id',
                't4.OP_S_name',

            )
            ->where('t1.OP_Pr_E_id', '=', 1);

        if ($date1) {
            $funnel->where('t1.OP_Pr_date', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('t1.OP_Pr_date', '<=', $date2);
        }

        if ($OP_Pr_title) {
            $funnel->where('t1.OP_Pr_title', 'like', "{$OP_Pr_title}%");
        }

        if ($OP_S_id) {
            $funnel->where('t1.OP_S_id', '=', $OP_S_id);
        }
        if ($OP_PrT_id) {
            $funnel->where('t1.OP_PrT_id', '=', $OP_PrT_id);
        }


        $funnel->orderBy('t1.OP_Pr_date', 'desc');
        if ($request->search) {
            $funnel->take(800);
        } else {

            $funnel->take(50);
        }
        $funnel = $funnel->get();





        $response = Response::json($funnel, 200);
        return $response;
    }
}
