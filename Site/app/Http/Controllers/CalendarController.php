<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class CalendarController extends Controller
{
    public function calendar($lng)
    {

        $lng = convert18n($lng);
        // $calendar;

        $plenary = DB::table('A_Calendar as t')
            ->join('Pl_Program as p', 'p.Pl_Prog_id', '=', 't.Cuid')
            ->leftjoin('Pl_Program_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_Prog_id', '=', 'p.Pl_Prog_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'pl')

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at')
            ->where('t.CDate', '>=', DB::raw('date(now())'))
            ->select(
                's.Pl_ProgL_value as title',
                't.CDate',
                't.CTimeFrom',
                't.CTime',
                't.Cuid as id',
                't.Cid',
                't.Ccm_id as nsCid',
            )->take(8)->get();



        $incoming = DB::table('A_Calendar as t')
            ->join('M_Event as p', 'p.M_Event_id', '=', 't.Cuid')
            ->join('M_Event_Lng as s', function ($join) use ($lng) {
                $join->on('s.M_Event_id', '=', 'p.M_Event_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'inc')

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at')
            ->where('t.CDate', '>=', DB::raw('date(now())'))
            ->select(
                's.M_EventL_title as title',
                's.M_EventL_body as body',
                't.CDate',
                't.CTimeFrom',
                't.CTime',
                't.Cuid as id',
                't.Cid',
                't.Ccm_id as nsCid',
                'p.VS_ID',
            )->take(8)->get();

        $com = DB::table('A_Calendar as t')
            ->join('A_Cm_Sit as p', 'p.A_Cm_Sitid', '=', 't.Cuid')
            ->leftjoin('V_Stream as v', 'v.VS_ID', '=', 'p.VS_ID')
            ->join('A_ns_Coll_Lng as s', function ($join) use ($lng) {
                $join->on('s.A_ns_C_id', '=', 'p.A_ns_C_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'cm')

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at')
            ->where('t.CDate', '>=', DB::raw('date(now())'))
            ->select(
                's.A_ns_CL_value_short as title',
                't.CDate',
                't.CTimeFrom',
                't.CTime',
                't.Cuid as id',
                't.Cid',
                't.Ccm_id as nsCid',
                'v.VS_ID',
            )->take(30)->get();


        $response = Response::json([
            'plenary' => $plenary,
            'incoming' => $incoming,
            'com' => $com,

        ], 200);


        return $response;
    }


    public function live($lng)
    {

        $lng = convert18n($lng);
        // $calendar;

        $count = DB::table('A_Calendar as t')

            ->whereNull('t.deleted_at')
            ->where('t.CDate', '=', DB::raw('date(now())'))
            ->where('t.CTime', '>=', DB::raw('time(now())'))
            ->where('t.CTimeFrom', '<=', DB::raw('time(now())'))
            ->where('t.Cid', '!=', 17889)
            // ->where('t.CType', '!=', 'inc')
            ->select()->count();


        // $plenary = DB::table('A_Calendar as t')
        //     ->join('Pl_Program as p', 'p.Pl_Prog_id', '=', 't.Cuid')
        //     ->join('V_Stream as v', 'v.VS_ID', '=', 'v.VS_ID')
        //     ->leftjoin('Pl_Program_Lng as s', function ($join) use ($lng) {
        //         $join->on('s.Pl_Prog_id', '=', 'p.Pl_Prog_id')
        //             ->where('s.C_Lang_id', '=',  $lng);
        //     })
        //     ->where('p.C_St_id', 1)
        //     ->where('t.CType', 'pl')
        //     ->where('v.VS_ID', 6)

        //     ->whereNull('t.deleted_at')
        //     ->whereNull('p.deleted_at')
        //     ->where('t.CDate', '=', DB::raw('date(now())'))
        //     ->where('t.CTime', '>=', now())
        //     ->where('t.CTimeFrom', '<=', now())
        //     ->select(
        //         's.Pl_ProgL_value as title',
        //         't.CDate',
        //         't.CTimeFrom',
        //         't.CTime',
        //         't.Cuid as id',
        //         't.Cid',
        //         't.Ccm_id as nsCid',
        //         'v.VS_URL',
        //         'v.VS_ID',
        //         'v.VS_hall',
        //     )->take(8)->get();





        // $com = DB::table('A_Calendar as t')
        //     ->join('A_Cm_Sit as p', 'p.A_Cm_Sitid', '=', 't.Cuid')
        //     ->join('V_Stream as v', 'v.VS_ID', '=', 'p.VS_ID')
        //     ->join('A_ns_Coll_Lng as s', function ($join) use ($lng) {
        //         $join->on('s.A_ns_C_id', '=', 'p.A_ns_C_id')
        //             ->where('s.C_Lang_id', '=',  $lng);
        //     })
        //     ->where('p.C_St_id', 1)

        //     ->whereNull('t.deleted_at')
        //     ->whereNull('p.deleted_at')
        //     ->where('t.CDate', '=', DB::raw('date(now())'))
        //     ->where('t.CTime', '>=', now())
        //     ->where('t.CTimeFrom', '<=', now())
        //     ->select(
        //         's.A_ns_CL_value_short as title',
        //         't.CDate',
        //         't.CTimeFrom',
        //         't.CTime',
        //         't.Cuid as id',
        //         't.Cid',
        //         't.Ccm_id as nsCid',
        //         'v.VS_URL',
        //         'v.VS_ID',
        //         'v.VS_hall',
        //     )->take(8)->get();


        $response = Response::json([
            'count' => $count,
            'plenary' => $this->livePlenary($lng, 1),
            // 'plenary' => [
            //     'title' => '<i>Програмата ще бъде публикувана след гласуването ѝ в пленарна зала</i>',
            //     'CDate' => '2021-12-08',
            //     'CDate' => '09:00:00',
            //     'CTime' => '14:00:00',
            //     'id' => '1189',
            //     'Cid' => '17923',
            //     'nsCid' => '',
            //     'VS_URL' => 'https://e105-ts.cdn.bg/parliament/fls/zplenarna.stream/playlist.m3u8',
            //     'VS_ID' => '6',
            //     'VS_hall' => 'Пленарна зала',
            //     'VS_code' => '',
            //     'VS_mime' => '',
            // ],

            'com' => $this->liveCom($lng, 1),
            'inc' => $this->liveEvent($lng, 1),
            'upcoming' => [
                'plenary' => $this->livePlenary($lng, 0),
                'com' => $this->liveCom($lng, 0),
                'inc' => $this->liveEvent($lng, 0),
            ],

        ], 200);


        return $response;
    }



    public function livePlenary($lng, $type)
    {
        $plenary = DB::table('A_Calendar as t')
            ->join('Pl_Program as p', 'p.Pl_Prog_id', '=', 't.Cuid')
            ->join('V_Stream as v', 'v.VS_ID', '=', 'v.VS_ID')
            ->leftjoin('Pl_Program_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_Prog_id', '=', 'p.Pl_Prog_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'pl')
            ->where('v.VS_ID', 6)

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at');


        if ($type == 1) {
            $plenary->where('t.CDate', '=', DB::raw('date(now())'))
                ->where('t.CTime', '>=', DB::raw('time(now())'))
                ->where('t.CTimeFrom', '<=', DB::raw('time(now())'));
        } else {
            // $plenary->where('t.CDate', '>', DB::raw('date(now())'));
            $plenary->where('t.CDate', '>', DB::raw('date(now())'));
            // $plenary->where('t.CTime', '>=', DB::raw('time(now())'));
        }
        // $plenary->where('t.CTimeFrom', '<=', now());
        $plenary->select(
            's.Pl_ProgL_value as title',
            't.CDate',
            't.CTimeFrom',
            't.CTime',
            't.Cuid as id',
            't.Cid',
            't.Ccm_id as nsCid',
            'v.VS_URL',
            'v.VS_ID',
            'v.VS_hall',
            'v.VS_code',
            'v.VS_mime',
        )->take(8);

        $plenary = $plenary->get();

        // dd($plenary);
        return $plenary;
    }
    public function livePlenary_man($lng, $type)
    {
        $plenary = DB::table('A_Calendar as t')
            ->join('Pl_Program as p', 'p.Pl_Prog_id', '=', 't.Cuid')
            ->join('V_Stream as v', 'v.VS_ID', '=', 'v.VS_ID')
            ->leftjoin('Pl_Program_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_Prog_id', '=', 'p.Pl_Prog_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('p.Pl_Prog_id', 1189)
            ->where('t.CType', 'pl')
            ->where('t.Cid', 17924)
            ->where('v.VS_ID', 6)


            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at');



        // $plenary->where('t.CTimeFrom', '<=', now());
        $plenary->select(
            's.Pl_ProgL_value as title',
            't.CDate',
            't.CTimeFrom',
            't.CTime',
            't.Cuid as id',
            't.Cid',
            't.Ccm_id as nsCid',
            'v.VS_URL',
            'v.VS_ID',
            'v.VS_hall',
            'v.VS_code',
            'v.VS_mime',
        )->take(1);

        $plenary = $plenary->get();


        return $plenary;
    }


    public function liveCom($lng, $type)
    {
        $com = DB::table('A_Calendar as t')
            ->join('A_Cm_Sit as p', 'p.A_Cm_Sitid', '=', 't.Cuid')
            ->join('V_Stream as v', 'v.VS_ID', '=', 'p.VS_ID')
            ->join('A_ns_Coll_Lng as s', function ($join) use ($lng) {
                $join->on('s.A_ns_C_id', '=', 'p.A_ns_C_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'cm')

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at');

        if ($type == 1) {
            $com->where('t.CDate', '=', DB::raw('date(now())'))
                ->where('t.CTime', '>=', now())
                ->where('t.CTimeFrom', '<=', now())
                ->orderBy('t.CTimeFrom', 'asc');
        } else {
            // $com->where('t.CDate', '>', DB::raw('date(now())'));
            $com->where('t.CDate', '>=', DB::raw('date(now())'));
            // $com->where('t.CTime', '>=', now());
        }
        $com->select(
            's.A_ns_CL_value_short as title',
            't.CDate',
            't.CTimeFrom',
            't.CTime',
            't.Cuid as id',
            't.Cid',
            't.Ccm_id as nsCid',
            'v.VS_URL',
            'v.VS_ID',
            'v.VS_hall',
            'v.VS_code',
            'v.VS_mime',
        )->take(30);

        $com = $com->get();

        return $com;
    }



    public function liveEvent($lng, $type)
    {
        $event = DB::table('A_Calendar as t')
            ->join('M_Event as e', 'e.M_Event_id', '=', 't.Cuid')
            ->join('V_Stream as v', 'v.VS_ID', '=', 'e.VS_ID')
            ->leftjoin('M_Event_Lng as el', function ($join) use ($lng) {
                $join->on('el.M_Event_id', '=', 'e.M_Event_id')
                    ->where('el.C_Lang_id', '=',  $lng);
            })
            ->where('e.C_St_id', 1)
            ->where('t.CType', 'inc')

            ->whereNull('t.deleted_at')
            ->whereNull('e.deleted_at');


        if ($type == 1) {
            $event->where('t.CDate', '=', DB::raw('date(now())'))
                ->where('t.CTime', '>=', now())
                ->where('t.CTimeFrom', '<=', now());
        } else {
            $event->where('t.CDate', '>=', DB::raw('date(now())'));
            $event->where('t.CTime', '>=', now());
        }
        // $event->where('t.CTimeFrom', '<=', now());
        $event->select(
            'el.M_EventL_title as title',
            'el.M_EventL_body as body',
            't.CDate',
            't.CTimeFrom',
            't.CTime',
            't.Cuid as id',
            't.Cid',
            't.Ccm_id as nsCid',
            'v.VS_URL',
            'v.VS_ID',
            'v.VS_hall',
            'v.VS_code',
            'v.VS_mime',
        )->take(8);

        $event = $event->get();


        return $event;
    }


    public function videoArchive($lng, $id)
    {
        $lng =  convert18n($lng);

        // $calendar;

        $videoArchive = DB::table('Video_Archive as v')
            ->leftjoin('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'v.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->whereNull('v.deleted_at')

            ->where('v.Vid', $id)
            ->select('v.Vidate', 'v.Vicount', 'c18n.A_ns_CL_value',)
            ->first();



        $convertVidate = str_replace('-', '_', $videoArchive->Vidate);
        $videoArchive->default = 'https://parliament.bg/Gallery/video/archive-' . $convertVidate . '_1.mp4';

        for ($i = 1; $i <= $videoArchive->Vicount; ++$i) {
            $playlist = ['item' => $i, 'file' => 'https://parliament.bg/Gallery/video/archive-' . $convertVidate . '_' . $i . '.mp4'];
            $videoArchive->playlist[] = $playlist;
        }






        $response = Response::json($videoArchive, 200);


        return $response;
    }
}
