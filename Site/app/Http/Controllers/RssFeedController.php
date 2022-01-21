<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class RssFeedController extends Controller
{

    public function feed($feed)
    {


        if ($feed == 'news') {
            $rss =  [
                'path' => 'news/ID',
                'string' => 'news',
                'description' => 'Новини от Народно събрание',
                'content' => $this->feedNews()

            ];
        } elseif ($feed == 'bills') {
            $rss =  [
                'path' => 'bills/ID',
                'string' => 'bills',
                'description' => 'Последни законопроекти от Народно събрание',
                'content' => $this->feedActs(1, 0)

            ];
        } elseif ($feed == 'laws') {
            $rss =  [
                'path' => 'laws/ID',
                'string' => 'laws',
                'description' => 'Последни закони от Народно събрание',
                'content' => $this->feedActs(1, 1)

            ];
        } elseif ($feed == 'ns_acts') {
            $rss =  [
                'path' => 'ns_acts/ID',
                'string' => 'ns_acts',
                'description' => 'Последни проекти на решения от Народно събрание',
                'content' => $this->feedActs(1, 0)

            ];
        } elseif ($feed == 'desision') {
            $rss =  [
                'path' => 'desision/ID',
                'string' => 'desision',
                'description' => 'Последни решения от Народно събрание',
                'content' => $this->feedActs(2, 1)

            ];
        } elseif ($feed == 'plenary') {
            $rss =  [
                'path' => 'plenaryprogram/ID',
                'string' => 'plenary',
                'description' => 'Програма за работата на Народното събрание',
                'content' => $this->feedPlenary()

            ];
        } else {

            $rss =  [
                'path' => '',
                'string' => '',
                'description' => '',
                'content' => []

            ];
        }

        // dd($rss);


        return response()->view('rss.feed', compact('rss'))->header('Content-Type', 'application/xml');
    }



    public function feedNews()
    {
        $lng = 1;
        // 
        $rss = DB::table('M_News as n')
            ->join('M_News_Lng as n18n', function ($join) use ($lng) {
                $join->on('n18n.M_News_id', '=', 'n.M_News_id')
                    ->where('n18n.C_Lang_id', '=',  $lng);
            })
            // ->join('M_News_Img as img', 'img.M_News_id', '=', 'n.M_News_id')

            ->where('n.C_St_id', 1)
            ->whereNull('n.deleted_at')
            ->whereNull('n18n.deleted_at')
            ->select(
                'n.M_News_date as rs_Date',
                'n.M_News_id as rs_Id',
                'n18n.M_NewsL_title as rs_Title',
                'n18n.M_NewsL_body as rs_Body',
                // 'img.M_News_id'
            )
            ->orderBy('n.M_News_lead', 'desc')
            ->orderBy('n.M_News_date', 'desc')
            ->orderBy('n.M_News_order', 'desc')
            ->limit(50)
            ->get();
        // dd($rss);
        return $rss;
    }


    public function feedActs($type, $stage)
    {


        $lng  = 1;
        $actList = DB::table('L_Acts as t1')
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 't1.L_Ses_id')
            ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                    ->where('t2.C_Lang_id', '=',  $lng);
            })
            ->where('t1.L_Act_T_id', $type);
        if ($stage) {
            $actList->where('t1.L_Act_dv_ID', '>', 0);
            $actList->where('s.A_ns_id', currentNS())
                ->select(
                    't1.L_Act_id  as rs_Id',
                    't1.L_Act_date as rs_Date',
                    't2.L_ActL_final  as rs_Title',
                    't2.L_ActL_final  as rs_Body',

                );
        } else {
            $actList->where('s.A_ns_id', currentNS())
                ->select(
                    't1.L_Act_id  as rs_Id',
                    't1.L_Act_date as rs_Date',
                    't2.L_ActL_title as rs_Title',
                    't2.L_ActL_title as rs_Body',

                );
        }

        // 

        $actList->orderBy('t1.L_Act_date', 'desc')
            ->orderBy('t1.sync_ID', 'desc')
            ->orderBy('t1.sync_ID', 'desc')
            ->limit(50)
            ->get();


        return $actList->get();
        // 

    }



    public function feedPlenary()
    {


        $lng  = 1;
        $feedPlenary = DB::table('A_Calendar as t')
            ->join('Pl_Program as p', 'p.Pl_Prog_id', '=', 't.Cuid')
            ->leftjoin('Pl_Program_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_Prog_id', '=', 'p.Pl_Prog_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->where('p.C_St_id', 1)
            ->where('t.CType', 'pl')

            ->whereNull('t.deleted_at')
            ->whereNull('p.deleted_at')
            // ->where('t.CDate', '>=', DB::raw('date(now())'))
            ->select(
                // 's.Pl_ProgL_value as rs_Title',
                // 's.Pl_ProgL_value as rs_Body',
                't.CDate as rs_Date',
                't.Cuid as rs_Id',
                DB::raw("'Програма за работата на Народно събрание'  as rs_Title"),
                DB::raw("'Програма за работата на Народно събрание'  as rs_Body"),
            )
            ->orderBy('t.CDate', 'desc')
            ->limit(10)
            ->get();






        return $feedPlenary;
    }
}
