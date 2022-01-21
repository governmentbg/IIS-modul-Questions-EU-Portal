<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class StudentsController extends Controller
{
    public function rsList()
    {
        // 

        $rsList = DB::table('St_Posts as r')
            ->leftjoin('St_Section as t', 't.StS_id', '=', 'r.StS_id')

            ->select(
                'r.StP_id',
                't.StS_id',
                't.StS_name',
                'r.StP_title',
                'r.StP_date',


            )
            ->where('r.StS_id', 1)
            ->whereNull('r.deleted_at')
            ->orderBy('r.StP_date', 'desc')
            ->orderBy('r.StP_id', 'desc')
            ->get();






        $response = Response::json($rsList, 200);
        return $response;
    }

    public function rsProfile($id)
    {
        // 

        $rsProfile = DB::table('St_Posts as r')
            ->leftjoin('St_Section as t', 't.StS_id', '=', 'r.StS_id')

            ->select(
                'r.StP_id',
                't.StS_id',
                't.StS_name',
                'r.StP_title',
                'r.StP_body',
                'r.StP_date',


            )
            ->where('r.StP_id', $id)
            ->whereNull('r.deleted_at')
            ->orderBy('r.StP_date', 'desc')
            ->orderBy('r.StP_id', 'desc')
            ->first();

        $rsProfile->files = DB::table('St_Files')
            ->where('StP_id', $rsProfile->StP_id)
            ->where('StF_type', 2)
            ->whereNull('deleted_at')

            ->select(
                'StF_id',
                'StF_name',
                DB::raw("IF(SUBSTR(StF_file, 1,4)='pub/',CONCAT('/',StF_file),CONCAT('/pub/students/',StF_file))  as StF_file"),

            )
            ->get();






        $response = Response::json($rsProfile, 200);
        return $response;
    }
}
