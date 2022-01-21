<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class TSController extends Controller
{
    public function tsList($lng, Request $request)
    {


        $archive = cleanUp($request->archive);
        $nsCid = cleanUp($request->nsCid);
        $lng =  convert18n($lng);

        $list =  DB::table('C_Theme_Site as ts')
            ->join('C_Theme_Site_Lng as tsl', function ($join) use ($lng) {
                $join->on('tsl.Cts_id', '=', 'ts.Cts_id')
                    ->where('tsl.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 'ts.A_ns_C_id')
            ->leftjoin('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                $join->on('c.A_ns_C_id', '=', 'ts.A_ns_C_id')
                    ->where('c.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_Assembly_Lng as ns', function ($join) use ($lng) {
                $join->on('ns.A_ns_id', '=', 'cl.A_ns_id')
                    ->where('ns.C_Lang_id', '=',  $lng);
            })


            ->select(
                'ns.A_ns_id',
                'ns.A_nsL_value',
                'ts.Cts_id',
                'ts.Cts_name',
                'ts.Cts_img',
                'ts.Cts_path',
                'ts.Cts_date',
                'ts.A_ns_C_id',
                'c.A_ns_CL_value',
                'tsl.CtsL_value',
                'tsl.CtsL_text',
                DB::raw("IF(SUBSTR(ts.Cts_img, 1,4)='pub/',CONCAT('/',ts.Cts_img),CONCAT('/pub/',ts.Cts_img))  as Cts_img")
            )
            ->where('ts.C_St_id', 1)
            ->whereNull('ts.deleted_at')
            ->orderBy('ts.Cts_date', 'desc')
            ->orderBy('ts.Cts_order', 'desc');
        if ($archive) {
            $list->where('ts.C_Arch_id', $archive);
        }

        // else {
        //     $list->where('ts.C_Arch_id', 1);
        // }

        if ($nsCid) {
            $list->where('ts.A_ns_C_id', $nsCid);
        }
        $list = $list->get();


        $response = Response::json($list, 200);
        return $response;
    }


    public function tsProfile($lng, $id, Request $request)
    {


        $archive = cleanUp($request->archive);
        $nsCid = cleanUp($request->nsCid);
        $lng =  convert18n($lng);

        $profile =  DB::table('C_Theme_Site as ts')
            ->join('C_Theme_Site_Lng as tsl', function ($join) use ($lng) {
                $join->on('tsl.Cts_id', '=', 'ts.Cts_id')
                    ->where('tsl.C_Lang_id', '=',  $lng);
            })

            ->leftjoin('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                $join->on('c.A_ns_C_id', '=', 'ts.A_ns_C_id')
                    ->where('c.C_Lang_id', '=',  $lng);
            })


            ->select(
                'ts.Cts_id',
                'ts.Cts_name',
                'ts.Cts_img',
                'ts.Cts_path',
                'ts.A_ns_C_id',
                'c.A_ns_CL_value',
                'tsl.CtsL_value',
                'tsl.CtsL_text',
                'tsl.CtsL_id',
                DB::raw("IF(SUBSTR(ts.Cts_img, 1,4)='pub/',CONCAT('/',ts.Cts_img),CONCAT('/pub/',ts.Cts_img))  as Cts_img")
            )
            ->where('ts.C_St_id', 1)
            ->where('ts.Cts_id', $id)
            ->whereNull('ts.deleted_at')
            ->orderBy('ts.Cts_order', 'desc');
        if ($archive) {
            $profile->where('ts.C_Arch_id', $archive);
        }

        // else {
        //     $profile->where('ts.C_Arch_id', 1);
        // }

        if ($nsCid) {
            $profile->where('ts.A_ns_C_id', $nsCid);
        }
        $profile = $profile->first();

        if ($profile) {
            // documents
            $profile->docs = $this->tsDocs($lng, $profile->Cts_id, 1, $profile->A_ns_C_id);
            $profile->stan = $this->tsDocs($lng, $profile->Cts_id, 2, $profile->A_ns_C_id);
            $profile->rules = $this->tsDocs($lng, $profile->Cts_id, 3, $profile->A_ns_C_id);



            // candidates
            $profile->candidates = DB::table('TS_A_ns_Mps as vp')

                ->join('TS_A_ns_Mps_Lng as ll', function ($join) use ($lng) {
                    $join->on('ll.A_ns_MP_id', '=', 'vp.A_ns_MP_id')
                        ->where('ll.C_Lang_id', '=',  $lng);
                })

                ->where('vp.Cts_id', $profile->Cts_id)

                ->select(
                    'vp.A_ns_MP_id',
                    'vp.A_ns_MP_signature',
                    'vp.A_ns_MP_date',
                    'll.A_ns_MPL_Name1',
                    'll.A_ns_MPL_Name2',
                    'll.A_ns_MPL_Name3',

                )
                ->orderBy('vp.A_ns_MP_id', 'asc')
                ->orderBy('ll.A_ns_MPL_Name1', 'asc')
                ->orderBy('ll.A_ns_MPL_Name2', 'asc')
                ->orderBy('ll.A_ns_MPL_Name3', 'asc')
                ->get();


            foreach ($profile->candidates as $key_parent1 => $mp) {
                $importer = DB::table('TS_A_ns_Mp_Importer as t')
                    ->join('A_ns_Mps_Lng as ll', function ($join) use ($lng) {
                        $join->on('ll.A_ns_MP_id', '=', 't.A_ns_MPD_id')
                            ->where('ll.C_Lang_id', '=',  $lng);
                    })

                    ->where('t.A_ns_MP_id', $mp->A_ns_MP_id)
                    ->whereNull('t.deleted_at')
                    ->select(
                        'll.A_ns_MP_id',
                        'll.A_ns_MPL_Name1',
                        'll.A_ns_MPL_Name2',
                        'll.A_ns_MPL_Name3',

                    )
                    ->get();


                $profile->candidates[$key_parent1]->importer = $importer;
            }



            // meetings
            $profile->meetings = DB::table('TS_A_Cm_Sit as t')

                ->join('A_ns_Coll_Lng as s', function ($join) use ($lng) {
                    $join->on('s.A_ns_C_id', '=', 't.A_ns_C_id')
                        ->where('s.C_Lang_id', '=',  $lng);
                })
                ->leftjoin('A_Cm_SitP_Lng as p', function ($join) use ($lng) {
                    $join->on('p.A_Cm_SitPid', '=', 't.A_Cm_SitPid')
                        ->where('p.C_Lang_id', '=',  $lng);
                })
                ->where('t.C_St_id', 1)

                ->whereNull('t.deleted_at')
                ->where('t.Cts_id', $id)
                ->select(
                    's.A_ns_CL_value_short',
                    'p.A_Cm_SitPL_value',
                    't.A_Cm_Sitid',
                    't.A_Cm_Sit_date',
                    't.A_ns_C_id',
                    't.A_Cm_Sit_room',
                    't.A_Cm_Sit_body_1',
                )
                ->get();


            $profile->meetings = $profile->meetings->map(
                function ($items) {

                    $data['A_ns_CL_value_short'] = $items->A_ns_CL_value_short;
                    $data['A_Cm_SitPL_value'] = $items->A_Cm_SitPL_value;
                    $data['A_Cm_Sitid'] = $items->A_Cm_Sitid;
                    $data['A_Cm_Sit_date'] = $items->A_Cm_Sit_date;
                    $data['A_ns_C_id'] = $items->A_ns_C_id;
                    $data['A_Cm_Sit_room'] = $items->A_Cm_Sit_room;
                    $data['A_Cm_Sit_body_1'] = nl2br($items->A_Cm_Sit_body_1);

                    return $data;
                }
            );


            //    reports
            $profile->reports = DB::table('TS_A_Cm_Stan as t')
                ->where('t.Cts_id', $id)
                ->whereNull('t.deleted_at')
                // ->where('t.A_ns_C_id', $id)
                // ->where('t.A_Cm_Stan_type', $index)
                ->select(
                    't.A_Cm_Stan_date',
                    't.A_Cm_Stan_sub_1 as A_Cm_Stan_sub',
                    't.A_Cm_Stan_text_1 as A_Cm_Stan_text',
                    't.A_Cm_Stanid',

                )

                ->get();

            $profile->reports = $profile->reports->map(
                function ($items) {

                    $data['A_Cm_Stan_date'] = $items->A_Cm_Stan_date;
                    $data['A_Cm_Stan_sub'] = $items->A_Cm_Stan_sub;
                    $data['A_Cm_Stan_text'] = nl2br($items->A_Cm_Stan_text);
                    $data['A_Cm_Stanid'] = $items->A_Cm_Stanid;

                    return $data;
                }
            );

            // steno
            $profile->steno = DB::table('TS_A_Cm_Steno as t')
                ->where('t.Cts_id', $id)
                ->whereNull('t.deleted_at')
                // ->where('t.A_ns_C_id', $id)
                ->select(
                    't.A_Cm_St_date',
                    't.A_Cm_St_sub_1 as A_Cm_St_sub',
                    't.A_Cm_St_text_1 as A_Cm_St_text',
                    't.A_Cm_Stid',

                )

                ->get();


            $profile->steno = $profile->steno->map(
                function ($items) {

                    $data['A_Cm_St_date'] = $items->A_Cm_St_date;
                    $data['A_Cm_St_sub'] = $items->A_Cm_St_sub;
                    $data['A_Cm_St_text'] = nl2br($items->A_Cm_St_text);
                    $data['A_Cm_Stid'] = $items->A_Cm_Stid;

                    return $data;
                }
            );
        }


        $response = Response::json($profile, 200);
        return $response;
    }


    public function tsDocs($lng, $tsId, $type, $nsCid)
    {


        return DB::table('TS_A_Cm_Doc as t')
            ->where('t.Cts_id', $tsId)
            ->where('t.C_Lang_id', $lng)
            // ->where('t.A_ns_C_id', $nsCid)
            ->where('t.A_Cm_DocT_id', $type)
            ->whereNull('t.deleted_at')
            ->select(
                't.A_Cm_Doc_file',
                't.A_Cm_Doc_name',
                't.A_Cm_Doc_date',
                't.A_Cm_Doc_id',
                DB::raw("IF(SUBSTR(t.A_Cm_Doc_file, 1,4)='pub/',CONCAT('/',t.A_Cm_Doc_file),CONCAT('/pub/cW/',t.A_Cm_Doc_file))  as A_Cm_Doc_file")

            )
            ->orderBy('t.A_Cm_Doc_date', 'desc')
            ->take(300)
            ->get();
    }
}
