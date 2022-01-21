<?php

namespace App\Http\Controllers\Api\v1;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class NavigationController extends Controller
{

    public function navList($lang, $type)
    {

        $lng = convert18n($lang);

        $nav = $this->navObject($lng, $type, NULL);

        // $new_nav = $nav;
        foreach ($nav as $key_parent => $n) {

            $nav1 = $this->navObject($lng, $type, $n->C_Nav_id);

            if ($nav1->isEmpty()) {
                // $nav[$key_parent]->sub_nav = [];
            } else {
                foreach ($nav1 as $key_parent1 => $n1) {

                    $nav2 = $this->navObject($lng, $type, $n1->C_Nav_id);

                    if ($nav2->isEmpty()) {
                        // $nav1[$key_parent1]->sub_nav = [];
                    } else {
                        $nav1[$key_parent1]->sub_nav = $nav2;
                    }


                    foreach ($nav2 as $key_child_1 => $n2) {

                        $nav3 = $this->navObject($lng, $type, $n2->C_Nav_id);
                        if ($nav3->isEmpty()) {
                            // $nav1[$key_parent1]->sub_nav = [];
                        } else {
                            $nav2[$key_child_1]->sub_nav = $nav3;
                        }
                    }
                }
                $nav[$key_parent]->sub_nav = $nav1;
            }
        }



        $response = Response::json($nav, 200);
        return $response;
    }


    public function navObject($lng, $type, $parent)
    {


        return DB::table('C_Navigation as nav')
            ->join('C_Navigation_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.C_Nav_id', '=', 'nav.C_Nav_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')
            // ->leftjoin('C_Nav_comp as cp', 'cp.C_Nav_string', '=', 'nav.C_Nav_string')
            ->where('nav.C_NavT_id', $type)
            ->where('nav.C_Nav_parent_id', $parent)
            ->where('nav.C_St_id', 1)
            // ->where('n18n.C_St_id', 1)
            ->whereNull('nav.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'nav.C_Nav_string',
                'nav.C_Nav_id',
                'n18n.C_NavL_value',
                'n18n.C_NavL_URL',
                'lng.C_Lang_string',
                DB::raw("(SELECT COUNT(*) FROM C_Navigation WHERE C_Nav_parent_id = nav.C_Nav_id and C_St_id=1) as snc")
                // 'cp.C_NavC_id',
                // 'cp.C_NavC_comp',
                // DB::raw("IF(cp.C_NavC_id>0,cp.C_NavC_comp,'Article')  as C_NavC_comp"),
                // 'atr.Atr_lov_name',
                // DB::raw("IF(LENGTH(nav.xxx)>0,'true','false')  as yyyy"),
            )
            ->orderBy('C_Nav_order', 'desc')
            ->get();
    }

    public function metaObject($lng, $string)
    {

        $lng = convert18n($lng);


        $meta = DB::table('C_Navigation as nav')
            ->join('C_Navigation_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.C_Nav_id', '=', 'nav.C_Nav_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')
            ->where('nav.C_Nav_string', $string)
            ->where('nav.C_St_id', 1)
            ->whereNull('nav.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'nav.C_Nav_string',
                'nav.C_Nav_parent_id',
                'nav.C_Nav_id',
                'n18n.C_NavL_value',
                'lng.C_Lang_string'
            )
            ->first();


        if ($meta) {
            // $meta->path = [];
            $meta->sub = DB::table('C_Navigation as nav')
                ->join('C_Navigation_Lng as n18n', function ($join) use ($lng) {
                    $join->on('n18n.C_Nav_id', '=', 'nav.C_Nav_id')
                        ->where('n18n.C_Lang_id', '=',  $lng);
                })
                ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')

                ->where('nav.C_Nav_parent_id', $meta->C_Nav_id)
                ->where('nav.C_St_id', 1)
                ->whereNull('nav.deleted_at')
                ->whereNull('n18n.deleted_at')
                ->select(
                    'nav.C_Nav_string',
                    'nav.C_Nav_id',
                    'n18n.C_NavL_value',
                    'n18n.C_NavL_URL',
                    'lng.C_Lang_string'
                )
                ->get();
            $meta->parent = DB::table('C_Navigation as nav')
                ->join('C_Navigation_Lng as n18n', function ($join) use ($lng) {
                    $join->on('n18n.C_Nav_id', '=', 'nav.C_Nav_id')
                        ->where('n18n.C_Lang_id', '=',  $lng);
                })
                ->join('C_Lang as lng', 'lng.C_Lang_id', '=', 'n18n.C_Lang_id')

                ->where('nav.C_Nav_id', $meta->C_Nav_parent_id)
                ->where('nav.C_St_id', 1)
                ->whereNull('nav.deleted_at')
                ->whereNull('n18n.deleted_at')
                ->whereNotNull('nav.C_Nav_parent_id')
                ->select(
                    'nav.C_Nav_string',
                    'nav.C_Nav_id',
                    'nav.C_Nav_parent_id',
                    'n18n.C_NavL_value',
                    'lng.C_Lang_string'
                )
                ->first();
        } else {
            $meta = [
                'C_Nav_string' => '404',
                'C_Nav_id' => '0',
                'C_NavL_value' => '',
                'C_Lang_string' => 'bg',
            ];
        }



        $response = Response::json($meta, 200);
        return $response;
    }
}
