<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class GalleryController extends Controller
{
    // latestEvents

    public function latestRecords($lng, $count)
    {
        // return 1;

        $lng = convert18n($lng);
        if ($count > 10) {
            $count = 10;
        }
        $galList = DB::table('G_Gallery_Events as ev')
            ->join('G_Gallery_Events_Lng as e18n', function ($join) use ($lng) {
                $join->on('e18n.G_Gal_E_id', '=', 'ev.G_Gal_E_id')
                    ->where('e18n.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'ev.M_News_id')
                    ->where('e18n.C_Lang_id', '=',  $lng);
            })


            ->select(
                'ev.G_Gal_E_id',
                'ev.G_Gal_E_date',
                'e18n.G_Gal_EL_value',
                'ev.M_News_id',
                'e18n.C_Lang_id',
                'n18n.M_NewsL_title',

            )
            ->whereNull('ev.deleted_at')
            ->whereNull('e18n.deleted_at')
            ->orderBy('ev.G_Gal_E_date', 'desc')
            ->orderBy('ev.G_Gal_E_id', 'desc')
            ->take($count)
            ->get();




        $response = Response::json($galList, 200);
        return $response;
    }


    // Gallery 

    public function gallery($lng, $id)
    {
        // return 1;

        $lng = convert18n($lng);

        $galDetails = DB::table('G_Gallery_Events as ev')
            ->join('G_Gallery_Events_Lng as e18n', function ($join) use ($lng) {
                $join->on('e18n.G_Gal_E_id', '=', 'ev.G_Gal_E_id')
                    ->where('e18n.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'ev.M_News_id')
                    ->where('e18n.C_Lang_id', '=',  $lng);
            })

            ->where('ev.G_Gal_E_id', $id)
            ->select(
                'ev.G_Gal_E_id',
                'ev.G_Gal_E_date',
                'e18n.G_Gal_EL_value',
                'ev.M_News_id',
                'e18n.C_Lang_id',
                'n18n.M_NewsL_title',


            )
            ->whereNull('ev.deleted_at')
            ->whereNull('e18n.deleted_at')
            ->orderBy('ev.G_Gal_E_date', 'desc')
            ->orderBy('ev.G_Gal_E_id', 'desc')
            ->first();

        $galDetails->gallery = DB::table('G_Gallery')


            ->where('G_Gal_E_id', $galDetails->G_Gal_E_id)
            ->select(
                'G_Gal_id',
                // 'G_Gal_file',
                DB::raw("IF(SUBSTR(G_Gal_file, 1,8)='Gallery/',CONCAT('/',G_Gal_file),CONCAT('https://www.parliament.bg/Gallery/',G_Gal_file))  as thumb"),
                DB::raw("IF(SUBSTR(G_Gal_file, 1,8)='Gallery/',CONCAT('/',G_Gal_file),CONCAT('https://www.parliament.bg/Gallery/',G_Gal_file))  as src"),

                DB::raw('CONCAT("' . $galDetails->G_Gal_EL_value . '","") as caption'),
                // DB::raw("SUBSTR(G_Gal_file, 1,8)"),


            )
            ->whereNull('deleted_at')
            ->get();

        // $galDetails->map =  $galDetails->gallery->map(
        //     function ($items) {

        //         $data['thumb'] = $items->G_Gal_full_path;
        //         $data['src'] = $items->G_Gal_full_path;
        //         $data['caption'] = $galDetails->G_Gal_EL_value;

        //         return $data;
        //     }
        // );




        $response = Response::json($galDetails, 200);
        return $response;
    }
}
