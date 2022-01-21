<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;


class CollectionController extends Controller
{


    public function comIncSit($lng)
    {

        $lng = convert18n($lng);



        $response = Response::json([
            'upcoming' => $this->comIncSitType($lng, 0),
            'week' => $this->comIncSitType($lng, 1),

        ], 200);


        return $response;
    }

    public function comIncSitType($lng, $type)
    {


        $com = DB::table('A_Cm_Sit as t')
            // ->join('A_Cm_Sit as p', 'p.A_Cm_Sitid', '=', 't.Cuid')
            ->join('A_ns_Coll_Lng as s', function ($join) use ($lng) {
                $join->on('s.A_ns_C_id', '=', 't.A_ns_C_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_Cm_SitP_Lng as p', function ($join) use ($lng) {
                $join->on('p.A_Cm_SitPid', '=', 't.A_Cm_SitPid')
                    ->where('p.C_Lang_id', '=',  $lng);
            })
            ->where('t.C_St_id', 1)

            ->whereNull('t.deleted_at');

        if ($type) {
            $com->whereBetween('t.A_Cm_Sit_date', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
        } else {
            $com->whereDate('t.A_Cm_Sit_date', '>=', Carbon::now());
        }


        // ->whereBetween('t.A_Cm_Sit_date', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()])
        $com->select(
            's.A_ns_CL_value_short',
            'p.A_Cm_SitPL_value',
            't.A_Cm_Sitid',
            't.A_Cm_Sit_date',
            't.A_ns_C_id',
            't.A_Cm_Sit_room',
            't.A_Cm_Sit_body',
            't.VS_ID',
        )
            ->orderBy('t.A_Cm_Sit_date', 'asc')
            ->take(30);

        $com = $com->get();





        foreach ($com as $key_parent1 => $mp) {
            $media = DB::table('A_Cm_Media as t')
                ->where('t.C_Lang_id', 1)
                ->whereNull('t.deleted_at')
                ->where('t.A_Cm_Sitid', $mp->A_Cm_Sitid)
                ->select(
                    't.A_Cm_Md_id',
                    't.A_Cm_Md_date',
                    't.A_Cm_Md_name',
                    DB::raw("IF(SUBSTR(t.A_Cm_Md_file, 1,4)='pub/',CONCAT('/',t.A_Cm_Md_file),CONCAT('/pub/cW/',t.A_Cm_Md_file))  as A_Cm_Md_file")

                )
                ->take(30)
                ->get();


            $com[$key_parent1]->media = $media;
        }



        $map = $com->map(
            function ($items) {

                $data['A_ns_CL_value_short'] = $items->A_ns_CL_value_short;
                $data['A_Cm_SitPL_value'] = $items->A_Cm_SitPL_value;
                $data['A_Cm_Sitid'] = $items->A_Cm_Sitid;
                $data['A_Cm_Sit_date'] = $items->A_Cm_Sit_date;
                $data['A_ns_C_id'] = $items->A_ns_C_id;
                $data['A_Cm_Sit_room'] = $items->A_Cm_Sit_room;
                $data['VS_ID'] = $items->VS_ID;
                $data['A_Cm_Sit_body'] = nl2br($items->A_Cm_Sit_body);
                $data['media'] = $items->media;

                return $data;
            }
        );



        // $response = Response::json($com, 200);
        // $response = Response::json($map, 200);


        // $response = Response::json($com, 200);


        return $map;
    }


    public function comInfo($lng, $id)
    {
        // contacts + rules
    }


    public function comActs($lng, $id, $index)
    {
        // list bills & acts
        // DB::raw("IF(atr.Atr_obligatory>0,'Y','N')"),

        $lng = convert18n($lng);

        $com = DB::table('L_Act_Distr as li')
            ->join('L_Acts as l', 'l.L_Act_id', '=', 'li.L_Act_id')
            ->join('L_Acts_Lng as ll', function ($join) use ($lng) {
                $join->on('ll.L_Act_id', '=', 'li.L_Act_id')
                    ->where('ll.C_Lang_id', '=',  $lng);
            })

            ->where('li.A_ns_C_id', $id)
            ->where('l.L_Act_T_id', $index)

            ->select(
                'l.L_Act_id',
                'll.L_ActL_title',
                'l.L_Act_sign',
                'l.L_Act_date',
                DB::raw("IF(l.L_Act_T_id=1,'bills','ns_acts') as path"),
            )
            ->orderBy('l.L_Act_date', 'desc')
            ->take(300)->get();


        $response = Response::json($com, 200);


        return $response;
    }

    public function comPastSit($lng, $id)
    {

        $lng = convert18n($lng);

        $com = DB::table('A_Cm_Sit as t')

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
            ->where('t.A_ns_C_id', $id)
            ->select(
                's.A_ns_CL_value_short',
                'p.A_Cm_SitPL_value',
                't.A_Cm_Sitid',
                't.A_Cm_Sit_date',
                't.A_ns_C_id',
                't.A_Cm_Sit_room',
                't.A_Cm_Sit_body',
            )
            ->orderBy('t.A_Cm_Sit_date', 'desc')
            ->take(5)
            ->get();



        foreach ($com as $key_parent1 => $mp) {
            $media = DB::table('A_Cm_Media as t')
                ->where('t.C_Lang_id', 1)
                ->whereNull('t.deleted_at')
                ->where('t.A_Cm_Sitid', $mp->A_Cm_Sitid)
                ->select(
                    't.A_Cm_Md_id',
                    't.A_Cm_Md_date',
                    't.A_Cm_Md_name',
                    DB::raw("IF(SUBSTR(t.A_Cm_Md_file, 1,4)='pub/',CONCAT('/',t.A_Cm_Md_file),CONCAT('/pub/cW/',t.A_Cm_Md_file))  as A_Cm_Md_file")

                )
                ->take(30)
                ->get();


            $com[$key_parent1]->media = $media;
        }



        $map = $com->map(
            function ($items) {

                $data['A_ns_CL_value_short'] = $items->A_ns_CL_value_short;
                $data['A_Cm_SitPL_value'] = $items->A_Cm_SitPL_value;
                $data['A_Cm_Sitid'] = $items->A_Cm_Sitid;
                $data['A_Cm_Sit_date'] = $items->A_Cm_Sit_date;
                $data['A_ns_C_id'] = $items->A_ns_C_id;
                $data['A_Cm_Sit_room'] = $items->A_Cm_Sit_room;
                $data['A_Cm_Sit_body'] = nl2br($items->A_Cm_Sit_body);
                $data['media'] = $items->media;

                return $data;
            }
        );



        // $response = Response::json($com, 200);
        $response = Response::json($map, 200);


        // $response = Response::json($com, 200);


        return $response;
    }

    public function comMeeting($lng, $id)
    {
        // ID meeting

        $lng = convert18n($lng);

        $com = DB::table('A_Cm_Sit as t')

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
            ->where('t.A_Cm_Sitid', $id)
            ->select(
                's.A_ns_CL_value_short',
                'p.A_Cm_SitPL_value',
                't.A_Cm_Sitid',
                't.A_Cm_Sit_date',
                't.A_ns_C_id',
                't.A_Cm_Sit_room',
                't.A_Cm_Sit_body',
            )
            ->first();



        if ($com) {

            $com->media = DB::table('A_Cm_Media as t')
                ->where('t.C_Lang_id', $lng)
                ->whereNull('t.deleted_at')
                ->where('t.A_Cm_Sitid', $id)
                ->select(
                    't.A_Cm_Md_id',
                    't.A_Cm_Md_date',
                    't.A_Cm_Md_name',
                    DB::raw("IF(SUBSTR(t.A_Cm_Md_file, 1,4)='pub/',CONCAT('/',t.A_Cm_Md_file),CONCAT('/pub/cW/',t.A_Cm_Md_file))  as A_Cm_Md_file")

                )
                ->take(30)
                ->get();

            $com->A_Cm_Sit_body = nl2br($com->A_Cm_Sit_body);
        }


        $response = Response::json($com, 200);


        return $response;
    }


    public function comPastNews($lng, $id)
    {

        $lng = convert18n($lng);

        $com = DB::table('A_Cm_News as t')
            ->where('t.C_Lang_id', $lng)
            ->whereNull('t.deleted_at')
            ->where('t.A_ns_C_id', $id)
            ->select(
                't.A_Cm_N_body',
                't.A_Cm_N_title',
                't.A_Cm_N_date',
                't.A_Cm_Nid',

            )
            ->orderBy('t.A_Cm_N_date', 'desc')
            ->take(2)
            ->get();


        $response = Response::json($com, 200);


        return $response;
    }

    public function comNews($lng, $id)
    {
        // ID meeting

        $lng = convert18n($lng);

        $com = DB::table('A_Cm_News as t')
            ->where('t.C_Lang_id', $lng)
            ->whereNull('t.deleted_at')
            ->where('t.A_Cm_Nid', $id)
            ->select(
                't.A_Cm_N_body',
                't.A_Cm_N_title',
                't.A_Cm_N_date',
                't.A_Cm_Nid',

            )
            ->first();


        $response = Response::json($com, 200);


        return $response;
    }



    public function comDocs($lng, $id)
    {
        $lng = convert18n($lng);

        $com = DB::table('A_Cm_Doc as t')
            ->where('t.C_Lang_id', $lng)
            ->whereNull('t.deleted_at')
            ->where('t.A_ns_C_id', $id)
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


        $response = Response::json($com, 200);


        return $response;
    }
    public function comStan($lng, $stId)
    {
        $lng = 1;

        $com = DB::table('A_Cm_Stan as t')
            ->leftjoin('L_Act_Status as s1', 's1.L_Act_St_id', '=', 't.L_Act_St_id')
            ->where('t.A_Cm_Stanid', $stId)
            ->where('t.C_St_id', 1)
            ->whereNull('t.deleted_at')
            // ->where('t.A_ns_C_id', $id)
            // ->where('t.A_Cm_Stan_type', $index)
            ->select(
                't.A_Cm_Stan_date',
                's1.L_Act_St_name',
                't.A_Cm_Stan_sub',
                't.A_Cm_Stan_text',
                't.A_Cm_Stanid',

            )

            ->first();

        $com->files = DB::table('A_Cm_Stan_File as li')


            ->where('li.A_Cm_Stanid', $com->A_Cm_Stanid)

            ->select(
                'li.A_Cm_StanF_id',
                // 'li.A_Cm_StanF_file',
                'li.A_Cm_StanF_name',
                DB::raw("IF(SUBSTR(li.A_Cm_StanF_file, 1,4)='pub/',CONCAT('/',li.A_Cm_StanF_file),CONCAT('/pub/cW/',li.A_Cm_StanF_file))  as A_Cm_StanF_file")

            )
            ->orderBy('li.A_Cm_StanF_id', 'asc')
            ->take(20)->get();

        return Response::json([
            'A_Cm_Stan_date' => $com->A_Cm_Stan_date,
            'L_Act_St_name' => $com->L_Act_St_name,
            'A_Cm_Stan_sub' => $com->A_Cm_Stan_sub,
            'A_Cm_Stan_text' => nl2br($com->A_Cm_Stan_text),
            'A_Cm_Stanid' => $com->A_Cm_Stanid,
            'files' => $com->files,

        ], 200);
    }

    public function comSteno($lng, $stId)
    {
        $lng = 1;

        $com = DB::table('A_Cm_Steno as t')
            ->where('t.A_Cm_Stid', $stId)
            ->whereNull('t.deleted_at')
            // ->where('t.A_ns_C_id', $id)
            ->select(
                't.A_Cm_St_date',
                't.A_Cm_St_sub',
                't.A_Cm_St_text',
                't.A_Cm_Stid',

            )

            ->first();


        $com->acts = DB::table('L_Act_Activity as li')
            ->join('L_Acts as l', 'l.L_Act_id', '=', 'li.L_Act_id')
            ->join('L_Acts_Lng as ll', function ($join) use ($lng) {
                $join->on('ll.L_Act_id', '=', 'li.L_Act_id')
                    ->where('ll.C_Lang_id', '=',  $lng);
            })

            ->where('li.A_Cm_Stid', $com->A_Cm_Stid)

            ->select(
                'l.L_Act_id',
                'll.L_ActL_title',
                'l.L_Act_sign',
                'l.L_Act_date',
                DB::raw("IF(l.L_Act_T_id=1,'bills','ns_acts') as path"),
            )
            ->orderBy('l.L_Act_date', 'desc')
            ->take(20)->get();

        return Response::json([
            'A_Cm_St_date' => $com->A_Cm_St_date,
            'A_Cm_St_sub' => $com->A_Cm_St_sub,
            'A_Cm_St_text' => nl2br($com->A_Cm_St_text),
            'A_Cm_Stid' => $com->A_Cm_Stid,
            'acts' => $com->acts,

        ], 200);
    }
}
