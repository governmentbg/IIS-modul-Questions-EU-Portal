<?php


namespace App\Http\Controllers;


use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class LinksController extends Controller
{
    public function linksList($lng)
    {
        $lng =  convert18n($lng);


        $list = DB::table('C_Links_Type as lt')
            ->join('C_Links_Type_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.C_LinkT_id', '=', 'lt.C_LinkT_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->select(
                'c18n.C_LinkTL_value',
                'lt.C_LinkT_id',

            )
            ->orderBy('lt.C_LinkT_prior', 'desc')
            ->get();



        foreach ($list as $key_parent1 => $ls) {
            $links = DB::table('C_Links as l')
                ->join('C_Links_Lng as ll', function ($join) use ($lng) {
                    $join->on('ll.C_Link_id', '=', 'l.C_Link_id')
                        ->where('ll.C_Lang_id', '=',  $lng);
                })
                ->join('C_Country_Lng as lc', function ($join) use ($lng) {
                    $join->on('lc.C_Country_id', '=', 'l.C_Country_id')
                        ->where('lc.C_Lang_id', '=',  $lng);
                })

                ->where('l.C_LinkT_id', $ls->C_LinkT_id)
                ->select(
                    'l.C_Link_id',
                    'l.C_Link_URL',
                    'lc.C_CountryL_value',
                    'll.C_LinkL_value',


                    // DB::raw("IF(SUBSTR(f.decl_file, 1,4)='pub/',CONCAT('/',f.decl_file),CONCAT('/pub/REG/',f.decl_file))  as decl_file"),
                    // 't.decl_type_id',
                    // 't.decl_type_text'
                )
                ->get();

            $map = $links->map(
                function ($items) {

                    $data['C_Link_id'] = $items->C_Link_id;
                    $data['C_CountryL_value'] = $items->C_CountryL_value;
                    $data['C_LinkL_value'] = $items->C_LinkL_value;
                    $data['C_Link_URL'] = urldecode($items->C_Link_URL);
                    return $data;
                }
            );




            $list[$key_parent1]->tLinks = $map;
        }


        $response = Response::json($list, 200);
        return $response;
    }
}
