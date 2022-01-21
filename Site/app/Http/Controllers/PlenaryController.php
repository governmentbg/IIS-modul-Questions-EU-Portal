<?php

namespace App\Http\Controllers;

use App\Models\PLRecordsLive;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class PlenaryController extends Controller
{
    public function plenaryProgram($lng, $id = 0)
    {
        $lng = convert18n($lng);

        if (!$id) {
            $id = DB::table('Pl_Program as t')
                ->join('Pl_Program_Lng as s', function ($join) use ($lng) {
                    $join->on('s.Pl_Prog_id', '=', 't.Pl_Prog_id')
                        ->where('s.C_Lang_id', '=',  $lng);
                })
                ->select('t.Pl_Prog_id')
                ->where('t.C_St_id', 1)

                ->whereNull('t.deleted_at')
                // ->whereNull('s.deleted_at')
                ->orderBy('t.Pl_Prog_date1', 'desc')
                // ->orderBy('t.Pl_Prog_id', 'desc')
                ->take(1)
                ->first()->Pl_Prog_id;
        }


        // $calendar;

        $plenary = DB::table('Pl_Program as t')
            ->join('Pl_Program_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_Prog_id', '=', 't.Pl_Prog_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->join('Pl_Program_Type_Lng as ptl', function ($join) use ($lng) {
                $join->on('ptl.Pl_ProgT_id', '=', 't.Pl_ProgT_id')
                    ->where('ptl.C_Lang_id', '=',  $lng);
            })
            ->join('Pl_Program_Status_Lng as st', function ($join) use ($lng) {
                $join->on('st.Pl_ProgS_id', '=', 't.Pl_ProgS_id')
                    ->where('st.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_Calendar as cal', function ($join) {
                $join->on('cal.Cuid', '=', 't.Pl_Prog_id')
                    ->where('cal.CType', '=',  'pl');
            })
            // ->leftjoin('A_Calendar as cal', 'cal.Cuid', '=', 't.Pl_Prog_id')
            ->where('t.C_St_id', 1)

            ->whereNull('t.deleted_at')
            // ->whereNull('s.deleted_at')
            ->where('t.Pl_Prog_id', $id)
            ->select(
                't.Pl_Prog_date1',
                't.Pl_Prog_date2',
                't.Pl_ProgT_id',
                's.Pl_ProgL_value',
                'cal.CTimeFrom',
                'cal.CTime',
                'ptl.Pl_ProgTL_value',
                'st.Pl_ProgSL_value',
                'st.Pl_ProgS_id',
                // 'st.*',
            )
            ->first();

        $Pl_points = DB::table('Pl_Program_Points as t')
            ->join('Pl_Program_Points_Lng as s', function ($join) use ($lng) {
                $join->on('s.Pl_ProgP_id', '=', 't.Pl_ProgP_id')
                    ->where('s.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_Coll_Lng as col', function ($join) use ($lng) {
                $join->on('col.A_ns_C_id', '=', 't.A_ns_C_id')
                    ->where('col.C_Lang_id', '=',  $lng);
            })

            // ->leftjoin('A_Calendar as cal', 'cal.Cuid', '=', 't.Pl_Prog_id')
            ->where('t.Pl_Prog_id', $id)
            ->orderBy('t.Pl_ProgNo', 'asc')


            ->select(
                'col.A_ns_C_id',
                'col.A_ns_CL_value',
                's.Name',
                't.Pl_Control_id',
                't.Pl_ProgP_id',
                't.Pl_ProgP_active',

            )
            ->get();


        $plenaryPoints = [];

        foreach ($Pl_points as $points) {

            $acts = DB::table('Pl_Program_Points_Act as t')
                ->join('L_Acts as t2', 't2.L_Act_id', '=', 't.L_Act_id')
                ->join('L_Acts_Lng as s', function ($join) use ($lng) {
                    $join->on('s.L_Act_id', '=', 't.L_Act_id')
                        ->where('s.C_Lang_id', '=',  $lng);
                })


                ->where('t.Pl_ProgP_id', $points->Pl_ProgP_id)
                ->where('t.sync_ID', '!=', 0)

                ->select(
                    't2.L_Act_id',
                    's.L_ActL_title',
                    't2.L_Act_sign',
                    't2.L_Act_T_id',

                )
                ->get();
            $documents = DB::table('Pl_Program_Points_Act as t')
                ->join('L_Act_Documents as t2', 't2.L_ActD_id', '=', 't.L_ActD_id')
                ->join('L_Act_Doc_Type as t3', 't3.L_ActD_T_id', '=', 't2.L_ActD_T_id')



                ->where('t.Pl_ProgP_id', $points->Pl_ProgP_id)
                // ->where('t.sync_ID', '=', 0)
                ->orderBy('t.Pl_ProgPA_id', 'asc')
                ->select(
                    'L_ActD_file',
                    't2.L_ActD_id',
                    'L_ActD_name',
                    't3.L_ActD_T_name',
                    't2.L_ActD_sign',

                )
                ->get();

            $plenaryPoints[] = [

                'A_ns_C_id' => $points->A_ns_C_id,
                'A_ns_CL_value' => $points->A_ns_CL_value,
                'Name' => $points->Name,
                'Pl_ProgP_active' => $points->Pl_ProgP_active,
                'Pl_Control_id' => $points->Pl_Control_id,
                'Pl_ProgP_id' => $points->Pl_ProgP_id,
                'acts' => $acts,
                'documents' => $documents,
            ];
        }

        $plenary->points =  $plenaryPoints;




        $response = Response::json($plenary, 200);


        return $response;
    }

    public function plenarySteno($id = 0)
    {

        if (!$id) {
            $id = DB::table('Pl_StenV as t')

                ->select('t.Pl_Sten_id')
                ->where('t.C_St_id', 1)


                ->orderBy('t.Pl_Sten_id', 'desc')
                ->first()->Pl_Sten_id;
        }


        // $calendar;

        $steno = DB::table('Pl_Sten as t')

            // ->leftjoin('A_Calendar as cal', 'cal.Cuid', '=', 't.Pl_Prog_id')
            ->where('t.C_St_id', 1)

            ->whereNull('t.deleted_at')
            // ->whereNull('s.deleted_at')
            ->where('t.Pl_Sten_id', $id)
            ->select(
                't.Pl_Sten_id',
                't.Pl_Sten_date',
                't.Pl_Sten_sub',
                't.Pl_Sten_body',

            )
            ->first();

        $steno->video = DB::table('Video_Archive')
            ->select('Vid', 'Vidate')
            ->where('Vidate', $steno->Pl_Sten_date)
            ->first();


        $steno->files = DB::table('Pl_StenD')
            ->select(
                'Pl_StenDid',
                'Pl_StenDname',
                'Pl_StenDfile',
                DB::raw("IF(SUBSTR(Pl_StenDfile, 1,4)='pub/',CONCAT('/',Pl_StenDfile),CONCAT('/pub/StenD/',Pl_StenDfile))  as Pl_StenDfile"),
                DB::raw("IF(SUBSTRING(Pl_StenDfile,-4)='.pdf',CONCAT('pdf'),CONCAT('xls'))  as Pl_StenDtype"),

            )
            ->where('Pl_Sten_id', $steno->Pl_Sten_id)
            ->get();


        // $response = Response::json($steno, 200);


        // return $response;
        return Response::json([
            'Pl_Sten_id' => $steno->Pl_Sten_id,
            'Pl_Sten_date' => $steno->Pl_Sten_date,
            'Pl_Sten_sub' => $steno->Pl_Sten_sub,
            'Pl_Sten_body' => nl2br($steno->Pl_Sten_body),
            'files' => $steno->files,
            'video' => $steno->video,

        ], 200);
    }




    public function plenaryLive(Request $request)
    {

        // dd($request);


        $lng = 1;
        $ns = currentNS();
        $nsCid = currentNSCID();



        DB::table('PL_Records_Live')->delete();

        $request->Topic = urldecode($request->Topic);
        $request->CurrentSpeechType = urldecode($request->CurrentSpeechType);
        // $request->Topic = 10;

        $PLRecordsLive = new PLRecordsLive(array(
            'StartDate' => $request->StartDate,
            'EndDate' => $request->EndDate,
            'Status' => $request->Status,
            'CurrentSpeechEgn' => $request->CurrentSpeechEgn,
            'CurrentSpeechType' => $request->CurrentSpeechType,
            'CurrentSpeechStartDate' => $request->CurrentSpeechStartDate,
            'CurrentVotingTypeLastRegisteredCount' => $request->CurrentVotingTypeLastRegisteredCount,
            'CurrentVotingTypeQuorumCount' => $request->CurrentVotingTypeQuorumCount,
            'CurrentVotingTypeVoteCount' => $request->CurrentVotingTypeVoteCount,
            'Topic' => $request->Topic,
            'CurrentRegistrationTypeRegisteredCount' => $request->CurrentRegistrationTypeRegisteredCount,
            'CurrentRegistrationTypeDeputyCount' => $request->CurrentRegistrationTypeDeputyCount,
            'CurrentRegistrationTypeIsCancelled' => $request->CurrentRegistrationTypeIsCancelled,
            'CurrentVotingTypeIsCancelled' => $request->CurrentVotingTypeIsCancelled,
            'CurrentVotingTypeIsSpecial' => $request->CurrentVotingTypeIsSpecial,
            'CurrentVotingTypeYesVotesRequired' => $request->CurrentVotingTypeYesVotesRequired,
            'CurrentVotingTypeYesCount' => $request->CurrentVotingTypeYesCount,
            'CurrentVotingTypeYesCount' => $request->CurrentVotingTypeYesCount,
            'CurrentVotingTypeNoCount' => $request->CurrentVotingTypeNoCount,
            'CurrentVotingTypeAbstainCount' => $request->CurrentVotingTypeAbstainCount,



        ));
        $PLRecordsLive->save();

        $live = DB::table('PL_Records_Live as r')
            ->join('PL_Records_Status as st', 'st.PL_RecS_id', '=', 'r.Status')


            ->leftjoin('A_ns_Mps as mp', function ($join) use ($ns) {
                $join->on('mp.A_ns_MP_EGN', '=', 'r.CurrentSpeechEgn')
                    ->where('mp.A_ns_id', '=',  $ns);
            })


            ->leftjoin('A_ns_Mps_Lng as mpl1', function ($join) use ($lng) {
                $join->on('mpl1.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl1.C_Lang_id', '=',  $lng);
            })

            // ->leftjoin('A_ns_MShip as ms', function ($join) {
            //     $join->on('ms.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            //         ->where('ms.A_ns_MSP_date_T', '=',  '9999-12-31');
            // })


            // ->leftjoin('A_ns_Coll as cl', function ($join) {
            //     $join->on('cl.A_ns_C_id', '=', 'ms.A_ns_C_id')
            //         ->where('cl.A_ns_CT_id', '=',  2);
            // })


            // ->leftjoin('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
            //     $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
            //         ->where('c18n.C_Lang_id', '=',  $lng);
            // })





            // ->where('Status', 500)
            ->orderBy(DB::raw('RAND()'))
            ->select(
                'r.ID',
                'r.StartDate',
                'r.EndDate',
                'r.CurrentSpeechType',
                'r.CurrentSpeechStartDate',
                'r.CurrentVotingTypeLastRegisteredCount',
                'r.CurrentVotingTypeQuorumCount',
                'r.CurrentVotingTypeVoteCount',
                'r.Topic',
                'r.CurrentRegistrationTypeRegisteredCount',
                'r.CurrentRegistrationTypeDeputyCount',
                'r.CurrentRegistrationTypeIsCancelled',
                'r.CurrentVotingTypeIsCancelled',
                'r.CurrentVotingTypeIsSpecial',
                'r.CurrentVotingTypeYesVotesRequired',
                'r.CurrentVotingTypeYesCount',
                'r.CurrentVotingTypeNoCount',
                'r.CurrentVotingTypeAbstainCount',

                'mpl1.A_ns_MPL_Name1',
                'mpl1.A_ns_MPL_Name2',
                'mpl1.A_ns_MPL_Name3',
                'mpl1.A_ns_MP_id',
                // 'c18n.A_ns_CL_value_short',



                'st.PL_RecS_id',
                'st.PL_RecS_name',

                // 'p.A_ns_MP_PosL_value',

            )
            ->first();

        // dd($live);
        if (!$live->A_ns_MPL_Name3) {
            $live->A_ns_MP_id = 0;
        }


        // return Response::json($live, 200);
        if ($request->file == 1) {
            $fp = fopen('live.json', 'w');
            fwrite($fp, json_encode($live));
            fclose($fp);
        } else {
            return Response::json($live, 200);
        }
    }
}


/*

?=Status=1&Topic=2&StartDate=&EndDate=&CurrentSpeechEgn=555555&CurrentSpeechSpeechType=&CurrentSpeechStartDate=&CurrentVotingTypeLastRegisteredCount=&CurrentVotingTypeQuorumCount=&CurrentVotingTypeVoteCount=&CurrentRegistrationTypeRegisteredCount=&CurrentRegistrationTypeDeputyCount=

*/
