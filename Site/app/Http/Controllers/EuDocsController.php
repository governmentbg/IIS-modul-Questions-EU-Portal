<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class EuDocsController extends Controller
{




    public function fnDocs(Request $request)
    {


        if (isset($request->docType)) {
            $docType =  (int) $request->docType["id"];
        } else {
            $docType =  null;
        }
        if (isset($request->areaType)) {
            $areaType =  (int) $request->areaType["id"];
        } else {
            $areaType =  null;
        }

        $euDocType = cleanUp($request->euDocType);

        // return $euDocType;
        $sign = cleanUp($request->sign);
        $title = cleanUp($request->title);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);



        $funnel = DB::table('EURO_ACT as t1')


            ->leftjoin('EURO_Markers as m1', 'm1.EM_id', '=', 't1.VID_DOSIE')
            ->leftjoin('EURO_Markers as m2', 'm2.EM_id', '=', 't1.AVTOR')
            // ->join('EURO_TEMA_ACT_EK as t3', 't3.ID_ACT', '=', 't1.ID')
        ;


        $funnel->select(
            't1.ID',
            't1.ZAGL_BG',
            't1.SIGN',
            't1.DAT_DOC',
        );



        // if ($docType=="eudocs") {
        //     $funnel->where('act.IME', 'like', '%' . $fnStringIme . '%');
        // }


        if ($euDocType == "eudocs") {
            $funnel->whereNotNull('SIGN_SERIA');

            if ($sign) {
                $funnel->where('t1.SIGN_NOMER', 'like', '%' . $sign . '%');
            }
        }

        if ($euDocType == "ceudocs") {
            $funnel->whereNull('SIGN_SERIA')->whereNull('AVTOR');

            if ($sign) {
                $funnel->where('t1.SIGN', 'like', '%' . $sign . '%');
            }
        }

        if ($euDocType == "euparldocs") {
            $funnel->whereNull('SIGN_SERIA')->where('AVTOR', 7098);

            if ($sign) {
                $funnel->where('t1.SIGN', 'like', '%' . $sign . '%');
            }
        }


        if ($date1) {
            $funnel->where('t1.DAT_DOC', '>=', $date1);
        }

        if ($date2) {
            $funnel->where('t2.DAT_DOC', '<=', $date2);
        }

        if ($areaType) {
            $funnel->where('t3.TEMA', '=', $areaType);
        }

        if ($docType) {
            $funnel->where('t1.VID_DOSIE', '=', $docType);
        }





        if ($title) {
            // $funnel->where('t1.ZAGL_BG', 'like', '%' . $title . '%');

            $funnel->where(function ($query) use ($title) {
                $query->where('t1.ZAGL_BG', 'like', "%{$title}%")
                    ->orWhere('t1.ZAGL_EN', 'like', "%{$title}%");
            });
        }






        $funnel->where('t1.DOST', 1);


        $funnel->orderBy('t1.DAT_DOC', 'desc');
        if ($request->search) {
            $funnel->take(400);
        } else {

            $funnel->take(100);
        }
        $funnel = $funnel->get();



        $response = Response::json($funnel, 200);
        return $response;
    }



    public function profile($id)
    {


        $profile = DB::table('EURO_ACT as t1')
            ->leftjoin('EURO_Markers as m1', 'm1.EM_id', '=', 't1.VID_DOSIE')
            ->leftjoin('EURO_Markers as m2', 'm2.EM_id', '=', 't1.AVTOR')
            ->select();



        // $funnel->where('d.ON_SITE_YESNO', '!', 2);
        $profile->where('t1.ID', $id)->where('t1.DOST', 1);
        // $profile->orderBy('d.ID', 'desc');
        $profile = $profile->first();
        if ($profile) {


            $profile->area = DB::table('EURO_TEMA_ACT_EK as t1')
                ->join('EURO_Markers as m1', 'm1.EM_id', '=', 't1.TEMA')
                ->where('t1.ID_ACT', $id)
                ->select(
                    't1.ID',
                    'm1.EM_name',
                )
                ->get();


            $profile->com = DB::table('EURO_RAZP_ACT_KOM as d')
                ->join('A_ns_Coll as c1', 'c1.sync_ID', '=', 'd.ID_KOMISIA')
                ->join('A_ns_Coll_Lng as c2', 'c2.A_ns_C_id', '=', 'c1.A_ns_C_id')
                ->where('c2.C_Lang_id', 1)
                ->where('d.ID_RAZPREDELIANE', $id)
                ->select(
                    'c2.A_ns_C_id',
                    'c2.A_ns_CL_value',

                )
                ->get();

            $profile->links = DB::table('EURO_LINKS_ACT')

                ->where('ID_ACT', $id)
                ->select(
                    'ID',
                    'URL',
                    'OPIS',
                )
                ->get();

            $profile->text = DB::table('EURO_ACT_FILES')

                ->where('ID_ACT', $id)
                ->select(
                    'ID',
                    'FILENAME',
                    DB::raw("CONCAT('/pub/ECD/',FILENAME)  as FILE_URL"),
                )
                ->get();
        }


        $response = Response::json($profile, 200);
        return $response;
    }

    public function optionsList()
    {
        // return 1;


        $contactList = [

            'docType' => $this->docType(),
            'areaType' => $this->areaType(),
            // 'com' => $this->colList(),


        ];

        $response = Response::json($contactList, 200);
        return $response;
    }



    public function docType()
    {
        return DB::table('EURO_ACT as t1')
            ->join('EURO_Markers as m1', 'm1.EM_id', '=', 't1.VID_DOSIE')
            ->select(
                'EM_id',
                'EM_name',
            )
            ->groupBy('EM_id')
            ->get();
    }

    public function areaType()
    {
        return DB::table('EURO_Markers')
            ->where('EM_code', 140)
            ->select(
                'EM_id',
                'EM_name',
            )
            ->groupBy('EM_id')
            ->get();
    }
}
