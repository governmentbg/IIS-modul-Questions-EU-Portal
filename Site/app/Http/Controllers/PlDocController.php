<?php

namespace App\Http\Controllers;


use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PlDocController extends Controller
{
    public function documentType()
    {

        $documentType = DB::table('L_Act_Doc_Type')
            ->orderBy('L_ActD_T_name', 'asc')->get();





        $response = Response::json($documentType, 200);
        return $response;
    }

    public function archiveDoc($year, $month)
    {

        $archiveDoc = DB::table('L_Act_Documents')
            ->select('L_ActD_date_ds')
            ->whereYear("L_ActD_date_ds", $year)
            ->whereMonth("L_ActD_date_ds", $month)
            ->groupBy('L_ActD_date_ds')
            ->orderBy('L_ActD_date_ds', 'asc')->get();

        $response = Response::json($archiveDoc, 200);
        return $response;
    }


    public function plDoc($id)
    {

        $document = DB::table('L_Act_Documents as d')
            ->leftjoin('L_Act_Doc_Type as t', 'd.L_ActD_T_id', '=', 't.L_ActD_T_id')
            ->select(
                'd.L_ActD_id',
                'd.L_ActD_T_id',
                't.L_ActD_T_name',
                'd.L_ActD_sign',
                'd.L_ActD_date_im',
                'd.L_ActD_date_ds',
                'd.L_ActD_name',
                'd.L_ActD_importer',
                DB::raw("IF(SUBSTR(d.L_ActD_file, 1,4)='pub/',CONCAT('/',d.L_ActD_file),CONCAT('/pub/plenary_documents/',d.L_ActD_file))  as L_ActD_file"),
            )
            ->where('d.L_ActD_id', '=', $id)
            ->first();


        $response = Response::json($document, 200);
        return $response;
    }

    public function fnDoc(Request $request)
    {

        // return $request;

        if (isset($request->L_ActD_T_id)) {
            $L_ActD_T_id =  (int) $request->L_ActD_T_id;
        } else {
            $L_ActD_T_id =  null;
        }



        $L_ActD_name = cleanUp($request->L_ActD_name);
        $L_ActD_sign = cleanUp($request->L_ActD_sign);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);


        $funnel = DB::table('L_Act_Documents as d')
            ->leftjoin('L_Act_Doc_Type as t', 'd.L_ActD_T_id', '=', 't.L_ActD_T_id')


            ->select(
                'd.L_ActD_id',
                'd.L_ActD_T_id',
                't.L_ActD_T_name',
                'd.L_ActD_sign',
                'd.L_ActD_date_im',
                'd.L_ActD_date_ds',
                'd.L_ActD_name',
                'd.L_ActD_importer',
                DB::raw("IF(SUBSTR(d.L_ActD_file, 1,4)='pub/',CONCAT('/',d.L_ActD_file),CONCAT('/pub/plenary_documents/',d.L_ActD_file))  as L_ActD_file"),



            );

        if ($date1) {
            $funnel->where('d.L_ActD_date_im', '=', $date1);
        }
        if ($date2) {
            $funnel->where('d.L_ActD_date_ds', '=', $date2);
        }

        if ($L_ActD_sign) {
            $funnel->where('d.L_ActD_sign', 'like', "{$L_ActD_sign}%");
        }
        if ($L_ActD_name) {
            $funnel->where('d.L_ActD_name', 'like', '%' . $L_ActD_name . '%');
        }
        if ($L_ActD_T_id) {
            $funnel->where('d.L_ActD_T_id', '=', $L_ActD_T_id);
        }
        if (!$request->search) {
            $latestDate = DB::table('L_Act_Documents')
                ->select('L_ActD_date_ds')
                ->orderBy('L_ActD_date_ds', 'desc')->first()->L_ActD_date_ds;
            $funnel->where('d.L_ActD_date_ds', '=', $latestDate);
        }

        $funnel->orderBy('d.L_ActD_date_ds', 'desc');
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
