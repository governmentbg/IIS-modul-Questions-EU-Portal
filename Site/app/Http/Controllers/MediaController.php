<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class MediaController extends Controller
{
    //

    public function frontNews($lng, $count)
    {

        $lng = convert18n($lng);
        if ($count > 10) {
            $count = 10;
        }



        $frontNews = DB::table('M_News as n')
            ->join('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'n.M_News_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            // ->join('M_News_Img as img', 'img.M_News_id', '=', 'n.M_News_id')
            ->whereExists(function ($query) {
                $query->select(DB::raw(1))
                    ->from('M_News_Img as img')
                    ->whereNull('img.deleted_at')
                    ->whereColumn('img.M_News_id', 'n.M_News_id');
            })
            ->where('n.C_St_id', 1)
            ->whereNull('n.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'n.M_News_date',
                'n.M_News_id',
                'n18n.M_NewsL_title',
                // 'img.M_News_id'
            )
            ->orderBy('n.M_News_lead', 'desc')
            ->orderBy('n.M_News_date', 'desc')
            ->orderBy('n.M_News_order', 'desc');

        if ($count == 1) {
            $frontNews = $frontNews->first();
            // $frontNews = $frontNews->where('n.M_News_lead', 1)->first();

            $frontNews->media = DB::table('M_News_Img')
                ->select(
                    'M_NewsMG_id',
                    // 'G_Gal_file',
                    DB::raw("IF(SUBSTR(M_NewsMG_file, 1,4)='pub/',CONCAT('/',M_NewsMG_file),CONCAT('/pub/Gallery/',M_NewsMG_file))  as M_NewsMG_file"),
                )
                ->where('M_News_id', $frontNews->M_News_id)
                ->whereNull('deleted_at')
                ->orderBy('M_NewsMG_id', 'desc')

                ->first();
        } else {
            $frontNews = $frontNews->skip(1)->take($count)->get();

            foreach ($frontNews as $list) {
                $media = DB::table('M_News_Img')
                    ->select(
                        'M_NewsMG_id',
                        // 'G_Gal_file',
                        DB::raw("IF(SUBSTR(M_NewsMG_file, 1,4)='pub/',CONCAT('/',M_NewsMG_file),CONCAT('/pub/Gallery/',M_NewsMG_file))  as M_NewsMG_file"),
                    )
                    ->where('M_News_id', $list->M_News_id)
                    ->whereNull('deleted_at')
                    ->orderBy('M_NewsMG_id', 'desc')

                    ->first();


                $frontNews1[] = [

                    'M_News_date' => $list->M_News_date,
                    'M_News_id' => $list->M_News_id,
                    'M_NewsL_title' => $list->M_NewsL_title,
                    'media' => $media,
                ];
            }
            $frontNews = $frontNews1;
        }
        // dd($frontNews);


        $response = Response::json($frontNews, 200);
        return $response;
    }



    public function showNews($lng, $id = 0)
    {

        $lng = convert18n($lng);

        if (!$lng or is_numeric($id) == false) {
            return Response::json([
                'status' => false,
                'message' => 'Record not found',
            ], 404);
        }

        if (!$id) {
            $id = DB::table('M_News as t')
                ->join('M_News_Lng as s', function ($join) use ($lng) {
                    $join->on('s.M_News_id', '=', 't.M_News_id')
                        ->where('s.C_Lang_id', '=',  $lng);
                })
                ->select('t.M_News_id')
                ->where('t.C_St_id', 1)

                ->whereNull('t.deleted_at')
                // ->whereNull('s.deleted_at')
                ->orderBy('t.M_News_id', 'desc')
                ->take(1)
                ->first()->M_News_id;
        }
        $news = DB::table('M_News as n')
            ->join('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'n.M_News_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            ->where('n.C_St_id', 1)
            ->where('n.M_News_id', $id)
            ->whereNull('n.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'n.M_News_date',
                'n.M_News_id',
                'n18n.M_NewsL_title',
                'n18n.M_NewsL_body',
                // 'img.M_News_id'
            )->first();

        if ($news) {


            $news->media = DB::table('M_News_Img')
                ->select(
                    'M_NewsMG_id',
                    // 'G_Gal_file',
                    DB::raw("IF(SUBSTR(M_NewsMG_file, 1,4)='pub/',CONCAT('/',M_NewsMG_file),CONCAT('/pub/Gallery/',M_NewsMG_file))  as M_NewsMG_file"),
                    // DB::raw("SUBSTR(G_Gal_file, 1,8)"),


                )
                ->where('M_News_id', $news->M_News_id)
                ->whereNull('deleted_at')
                ->orderBy('M_NewsMG_id', 'desc')

                ->first();


            $news->gallery = DB::table('G_Gallery as g')
                ->join('G_Gallery_Events as ev', 'ev.G_Gal_E_id', '=', 'g.G_Gal_E_id')


                ->where('ev.M_News_id', $news->M_News_id)
                ->whereNull('g.deleted_at')
                ->whereNull('ev.deleted_at')
                ->select(
                    // 'g.G_Gal_id',
                    // 'G_Gal_file',
                    DB::raw("IF(SUBSTR(g.G_Gal_file, 1,8)='Gallery/',CONCAT('/',g.G_Gal_file),CONCAT('https://www.parliament.bg/Gallery/',G_Gal_file))  as thumb"),
                    DB::raw("IF(SUBSTR(g.G_Gal_file, 1,8)='Gallery/',CONCAT('/',g.G_Gal_file),CONCAT('https://www.parliament.bg/Gallery/',G_Gal_file))  as src"),

                    DB::raw('CONCAT("","") as caption'),
                    // DB::raw('CONCAT("' . $news->M_NewsL_title . '","") as caption'),
                    DB::raw('CONCAT("","") as type'),
                    // DB::raw("SUBSTR(G_Gal_file, 1,8)"),


                )


                ->get();
        }


        // dd($frontNews);


        $response = Response::json($news, 200);
        return $response;
    }


    public function fnNews($lng, Request $request)
    {
        $lng = convert18n($lng);

        $funnel_string = cleanUp($request->string);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);


        $funnel = DB::table('M_News as n')
            ->join('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'n.M_News_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            ->where('n.C_St_id', 1)
            ->whereNull('n.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'n.M_News_date',
                'n.M_News_id',
                'n18n.M_NewsL_title',
                // 'n18n.M_NewsL_body',
                // 'img.M_News_id'
            );

        if ($date1) {
            $funnel->where('n.M_News_date', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('n.M_News_date', '<=', $date2);
        }

        if ($funnel_string) {
            $funnel->where(function ($query) use ($funnel_string) {
                $query->where('n18n.M_NewsL_title', 'like', '%' . $funnel_string . '%')
                    ->orWhere('n18n.M_NewsL_body', 'like', '%' . $funnel_string . '%');
            });
        }


        $funnel->orderBy('n.M_News_date', 'desc');
        $funnel->take(300);
        // if ($request->search) {
        // } else {

        //     $funnel->take(30);
        // }
        $funnel = $funnel->get();





        $response = Response::json($funnel, 200);
        return $response;
    }
}
