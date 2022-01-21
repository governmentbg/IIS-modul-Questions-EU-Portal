<?php

namespace App\Http\Controllers\Api\v1;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Validator;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class ArticleController extends Controller
{


    public function getKey($lang, $key)
    {
        $nav = DB::table('C_Navigation as nav')
            ->join('C_Navigation_Lng as n18n', 'n18n.C_Nav_id', '=', 'nav.C_Nav_id')
            ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')
            ->where('nav.C_Nav_string', $key)
            ->where('lng.C_Lang_string', $lang)
            ->where('lng.C_St_id', 1)
            ->where('nav.C_St_id', 1)
            ->exists();

        if (!$nav) {
            $response = Response::json([
                'C_ArL_title' => '404 page not found',
                'C_NavL_value' => '404 page not found',
                'C_ArL_body' => '',
            ], 200);

            return $response;
        } else {
            return $this->getArticle($lang, $key);
        }
    }

    public function getArticle($lang, $key)
    {
        // return 1;
        $lng = DB::table('C_Lang')
            ->where('C_Lang_string', $lang)
            ->select('C_Lang_id')
            ->first()->C_Lang_id;

        $article = DB::table('C_Navigation as nav')
            ->join('C_Navigation_Lng as n18n', 'n18n.C_Nav_id', '=', 'nav.C_Nav_id')
            ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')
            ->leftjoin('C_Articles as ar', 'ar.C_Nav_id', '=', 'nav.C_Nav_id')
            ->leftjoin('C_Articles_Lng as ar18n', function ($join) use ($lng) {
                $join->on('ar18n.C_Ar_id', '=', 'ar.C_Ar_id')
                    ->where('ar18n.C_Lang_id', '=',  $lng);
            })
            ->where('nav.C_Nav_string', $key)
            ->where('lng.C_Lang_id', $lng)
            ->where('lng.C_St_id', 1)
            ->select(
                'ar18n.C_ArL_title',
                'ar18n.C_ArL_body',
                'n18n.C_NavL_value'
            )
            ->first();


        $article->C_ArL_body = htmlspecialchars_decode($article->C_ArL_body, ENT_QUOTES);

        $article->list = DB::table('C_Article_List as nav')

            ->where('C_Nav_string', $key)
            ->where('C_Lang_id', $lng)
            ->where('C_St_id', 1)
            ->select(
                'C_AL_id',
                // 'C_AL_file',
                'C_AL_link',
                'C_AL_name',
                'C_AL_date',
                DB::raw("IF(SUBSTR(C_AL_file, 1,4)='pub/',CONCAT('/',C_AL_file),CONCAT('/pub/',C_AL_file))  as C_AL_file"),
                DB::raw("IF(SUBSTRING(C_AL_file,-4)='.pdf',CONCAT('pdf'),CONCAT('xls'))  as C_AL_file_type"),
            )
            ->orderBy('C_AL_order', 'desc')
            ->orderBy('C_AL_date', 'desc')
            ->orderBy('C_AL_id', 'desc')
            ->get();

        // dd($article);


        // $response = Response::json([
        //     'C_ArL_title' => $article->C_ArL_title,
        //     'C_NavL_value' => $article->C_NavL_value,
        //     'C_ArL_body' => htmlspecialchars_decode($article->C_ArL_body, ENT_QUOTES),
        // ], 200);
        $response = Response::json($article, 200);
        return $response;
    }


    public function getArticlePost($lang, $key, $subkey)
    {
        // return 1;
        $lng = DB::table('C_Lang')
            ->where('C_Lang_string', $lang)
            ->select('C_Lang_id')
            ->first()->C_Lang_id;

        $article = DB::table('C_Article_Post')
            ->where('C_ArP_key', $key)
            ->where('C_Lang_id', $lng)
            ->where('C_ArP_sub_key', $subkey)

            ->select(
                'C_ArP_id',
                'C_ArP_key',
                'C_ArP_sub_key',
                'C_ArP_title',
                'C_ArP_body',
            )
            ->first();

        if ($article) {

            $article->C_ArP_body = htmlspecialchars_decode($article->C_ArP_body, ENT_QUOTES);
        }




        // dd($article);



        $response = Response::json($article, 200);
        return $response;
    }
}
