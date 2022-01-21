<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class NciomController extends Controller
{

    public function rsList()
    {
        // 

        $rsList = DB::table('Nc_research as r')
            ->leftjoin('Nc_type as t', 't.Nc_rsT_id', '=', 'r.Nc_rsT_id')

            ->select(
                'r.Nc_rs_id',
                't.Nc_rsT_id',
                't.Nc_rsT_name',
                'r.Nc_rs_name',
                'r.Nc_rs_date',
                'r.Nc_rs_file',

                DB::raw("IF(SUBSTR(r.Nc_rs_file, 1,4)='pub/',CONCAT('/',r.Nc_rs_file),CONCAT('/pub/ncpi/',r.Nc_rs_file))  as Nc_rs_file"),
            )
            ->whereNull('r.deleted_at')
            ->orderBy('r.Nc_rs_date', 'desc')
            ->orderBy('r.Nc_rs_id', 'desc')
            ->get();






        $response = Response::json($rsList, 200);
        return $response;
    }
}
