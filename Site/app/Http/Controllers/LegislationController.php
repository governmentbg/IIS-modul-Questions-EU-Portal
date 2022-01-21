<?php

namespace App\Http\Controllers;

use App\Models\LActDiscuss;
use Carbon\Carbon;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator as FacadesValidator;
use Illuminate\Validation\Validator;

class LegislationController extends Controller
{


    public function frontRecords()
    {
        // return 1;

        $limit = DB::table('C_Configuration')
            ->where('CC_key', 'FRONT_PAGE_BILLS_COUNT')
            ->select('CC_value')
            ->first()->CC_value;




        $actList = [

            'bills' => $this->latestRecords(1, 0, $limit),
            'laws' => $this->latestRecords(1, 1, $limit),
            'trans' => $this->latestTrans(10),

        ];

        $response = Response::json($actList, 200);
        return $response;
    }


    public function latestTrans($limit)
    {
        // return 1;

        $actList = DB::table('EURO_DOSSIER as d')

            ->join('EURO_ACT_NEW as act', 'act.ID', '=', 'd.EURO_ACT_NEW_ID')
            ->join('EURO_ACT_Type as t', 't.ActT_id', '=', 'act.VID_ACT');
        //   $actList ->where('d.ON_SITE_YESNO', '!', 2);

        $actList->select(
            'd.ID',
            'd.NАМЕ as dName',
            'act.RN_FULL',
            't.ActT_name',

        );
        // 

        $actList->orderBy('d.ZAKON_DV_GOD_V', 'desc')
            ->orderBy('d.ZAKON_DV_BR_V', 'desc')

            ->take($limit);


        return $actList->get();


        // return $actList;
    }
    public function latestRecords($type, $stage = 0, $limit)
    {
        // return 1;

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
                    't1.L_Act_id',
                    't1.L_Act_date',
                    't2.L_ActL_final as L_ActL_title',

                );
        } else {
            $actList->where('s.A_ns_id', currentNS())
                ->select(
                    't1.L_Act_id',
                    't1.L_Act_date',
                    't2.L_ActL_title',

                );
        }

        // 

        $actList->orderBy('t1.L_Act_date', 'desc')
            ->orderBy('t1.sync_ID', 'desc')
            ->orderBy('t1.sync_ID', 'desc')
            ->take($limit);


        return $actList->get();


        // return $actList;
    }


    public function billsProfile($id)
    {
        // return 1;
        // return config('custom.APP_BILLS_PATH');

        $lng  = 1;
        $act = DB::table('L_Acts as t1')
            ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                    ->where('t2.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions_Lng as s1', function ($join) use ($lng) {
                $join->on('s1.L_Ses_id', '=', 't1.L_Ses_id')
                    ->where('s1.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 't1.L_Ses_id')
            ->join('A_ns_Assembly as s3', 's3.A_ns_id', '=', 's.A_ns_id')
            ->where('t1.L_Act_id', $id)
            // ->where('t1.C_St_id', 1)
            ->where('t1.L_Act_T_id', 1)
            ->select(
                't1.L_Act_id',
                't1.L_Act_sign',
                't1.L_Act_new_id',
                't1.L_Act_date',
                't1.L_act_proposal_date',
                't1.L_act_proposal_date2',
                't1.sync_ID',
                't1.L_Act_dv_ID',
                't1.L_Act_date2',
                't1.L_Act_dv_iss',
                't1.L_Act_dv_year',
                't2.L_ActL_title',
                't2.L_ActL_final',
                's3.A_ns_folder',
                's1.L_SesL_value',
            );



        $act = $act->first();
        // return  $act;

        if ($act) {



            $act->union_label = 1;
            if (DB::table('L_Acts as t1')
                ->where('t1.L_Act_new_id', $id)
                ->exists()
            ) {
                $act->union_label = 'Обединен законопроект, изготвен по реда на чл. 81, ал.2 от ПОДНС, на основата на приетите на първо гласуване:';
            } else {
                $act->union_label = '';
            }


            $act->union_list = DB::table('L_Acts as t1')
                ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                    $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                        ->where('t2.C_Lang_id', '=',  $lng);
                })
                ->where('t1.L_Act_new_id', $id)
                ->select('t1.L_Act_id', 't2.L_ActL_title')
                ->get();


            if ($act->L_Act_new_id > 0) {
                $act->trans_label = 'Обединен в законопроект, изготвен по реда на чл. 81, ал.2 от ПОДНС, на основата на приетите на първо гласуване:';
            } else {
                $act->trans_label = '';
            }

            $act->trans_list = DB::table('L_Acts as t1')
                ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                    $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                        ->where('t2.C_Lang_id', '=',  $lng);
                })
                ->where('t1.L_Act_id', $act->L_Act_new_id)
                ->select('t1.L_Act_id', 't2.L_ActL_title')
                ->get();

            $act->file_list = DB::table('L_Act_Files')

                ->where('L_Act_id', $act->sync_ID)
                ->where('VID_DOC', 827)
                ->where('FILENAME', 'like', "%{$act->L_Act_sign}%")
                // ->where('C_St_id', '!=', 2)
                ->where(function ($query) {
                    $query->whereNull('C_St_id')
                        ->orWhere('C_St_id',  1);
                })
                ->select('FILENAME', 'ID')
                ->get();

            $act->static_file = '';
            $act->static_archive = '';

            if (count($act->file_list) == 0) {

                // проверка за статичен файл синхронизиран с Министерски съвет
                if (file_exists(config('custom.APP_BILLS_PATH') . $act->A_ns_folder . '/' . $act->L_Act_sign . '.pdf')) {
                    $act->static_file =  $act->L_Act_sign . '.pdf';
                }


                // статичен rtf файл
                if (file_exists(config('custom.APP_BILLS_PATH') . $act->A_ns_folder . '/' . $act->L_Act_sign . '.rtf')) {
                    $act->static_file =  $act->L_Act_sign . '.rtf';
                }
            }



            // статична архивирана папка
            if (file_exists(config('custom.APP_BILLS_PATH') . $act->A_ns_folder . '/' . $act->L_Act_sign . '.zip')) {
                $act->static_archive = $act->L_Act_sign . '.zip';
            }



            $act->imp_list = DB::table('L_Acts_Importer as li')
                ->join('A_ns_Mps_Lng as mpl3', function ($join) use ($lng) {
                    $join->on('mpl3.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                        ->where('mpl3.C_Lang_id', '=',  $lng);
                })
                ->where('li.L_Act_id', $id)
                ->orderBy('li.sync_ID', 'asc')
                ->select('mpl3.A_ns_MP_id', 'mpl3.A_ns_MPL_Name1', 'mpl3.A_ns_MPL_Name2', 'mpl3.A_ns_MPL_Name3')
                ->get();

            $act->imp_list_min = DB::table('L_Acts_Importer as li')

                ->where('li.L_Act_id', $id)
                ->where('li.A_ns_C_id', 6167)
                ->select('li.A_ns_C_id')
                ->get();


            $act->dist_list = DB::table('L_Act_Distr as d')
                ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 'd.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 'd.A_ns_C_id')
                ->join('L_Act_DT_Distr_Type_Lng as dt', function ($join) use ($lng) {
                    $join->on('dt.L_Act_DT_id', '=', 'd.L_Act_DT_id')
                        ->where('dt.C_Lang_id', '=',  $lng);
                })
                ->where('d.L_Act_id', $id)
                ->where('cl.C_St_id', 1)
                ->orderBy('dt.L_Act_DTL_id', 'asc')
                ->orderBy('d.sync_ID', 'asc')
                ->select('d.L_Act_D_id', 'c.A_ns_C_id', 'c.A_ns_CL_value', 'dt.L_Act_DTL_value')
                ->get();


            $act->stan_list = DB::table('A_Cm_Stan as s')

                ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 's.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 's.L_Act_St_id')
                ->where('s.L_Act_id', $id)
                ->where('s.A_Cm_Stan_type', 1)
                // ->where('s1.L_Act_St_id', '!=', 3)
                ->where('s1.L_Act_St_id', '!=', 23)
                ->whereNull('s.deleted_at')
                ->select(
                    's.A_Cm_Stan_date',
                    's.A_Cm_Stanid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's.A_Cm_Stan_sub',
                    // 's.A_Cm_Stan_text'

                )
                ->get();

            $act->stan_list2 = DB::table('A_Cm_Stan as s')

                ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 's.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 's.L_Act_St_id')
                ->where('s.L_Act_id', $id)
                ->where('s.A_Cm_Stan_type', 1)
                ->where('s1.L_Act_St_id', '=', 23)
                // ->where('s1.L_Act_St_id', '=', 3)
                ->whereNull('s.deleted_at')
                ->select(
                    's.A_Cm_Stan_date',
                    's.A_Cm_Stanid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's.A_Cm_Stan_sub',
                    // 's.A_Cm_Stan_text'

                )
                ->get();

            $act->stan_list2_1 = DB::table('A_Cm_Stan as s')

                ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 's.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 's.L_Act_St_id')
                ->where('s.L_Act_id', $id)
                ->where('s.A_Cm_Stan_type', 1)
                ->where('s1.L_Act_St_id', '=', 3)
                ->whereNull('s.deleted_at')
                ->select(
                    's.A_Cm_Stan_date',
                    's.A_Cm_Stanid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's.A_Cm_Stan_sub',
                    // 's.A_Cm_Stan_text'

                )
                ->get();

            $act->standp_list = DB::table('A_Cm_Stan as s')

                ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 's.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 's.L_Act_St_id')
                ->where('s.L_Act_id', $id)
                ->where('s.A_Cm_Stan_type', 2)
                ->where('s1.L_Act_St_id', '!=', 23)
                ->whereNull('s.deleted_at')
                ->select(
                    's.A_Cm_Stan_date',
                    's.A_Cm_Stanid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's.A_Cm_Stan_sub',
                    // 's.A_Cm_Stan_text'

                )
                ->get();

            $act->steno_com = DB::table('L_Act_Activity as a')

                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 'a.L_Act_St_id')
                ->join('L_Act_Status2 as s2', 's2.L_Act_St2_id', '=', 'a.L_Act_St2_id')
                ->join('A_Cm_Steno as st', 'st.A_Cm_Stid', '=', 'a.A_Cm_Stid')
                ->leftjoin('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 'st.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->where('a.L_Act_id', $id)
                ->where('a.A_Cm_Stid', '>', 0)

                ->orderBy('a.L_Act_St_date')
                ->select(
                    'a.L_Act_A_id',
                    'a.L_Act_St_date',
                    'a.A_Cm_Stid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's2.L_Act_St2_name',

                )

                ->get();
            $act->steno_hall = DB::table('L_Act_Activity as a')

                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 'a.L_Act_St_id')
                ->join('L_Act_Status2 as s2', 's2.L_Act_St2_id', '=', 'a.L_Act_St2_id')

                ->where('a.L_Act_id', $id)
                ->where('a.Pl_Sten_id', '>', 0)

                ->orderBy('a.L_Act_St_date')
                ->select(
                    'a.L_Act_A_id',
                    'a.L_Act_St_date',
                    'a.Pl_Sten_id',
                    's1.L_Act_St_name',
                    's2.L_Act_St2_name',
                    // 's.A_Cm_Stan_text'

                )
                ->get();

            $act->activity = DB::table('L_Act_Activity as a')

                ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 'a.L_Act_St_id')
                ->join('L_Act_Status2 as s2', 's2.L_Act_St2_id', '=', 'a.L_Act_St2_id')
                ->leftjoin('A_Cm_Steno as st', 'st.A_Cm_Stid', '=', 'a.A_Cm_Stid')
                ->leftjoin('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                    $join->on('c.A_ns_C_id', '=', 'st.A_ns_C_id')
                        ->where('c.C_Lang_id', '=',  $lng);
                })
                ->where('a.L_Act_id', $id)
                ->where('a.L_Act_St_date', '!=', '0001-01-01')

                // ->groupBy('s1.L_Act_St_id')
                // ->groupBy('s2.L_Act_St2_id')
                ->orderBy('a.L_Act_St_date', 'asc')
                ->orderBy('s1.L_Act_St_order', 'desc')
                ->orderBy('s2.sync_ID', 'desc')
                ->orderBy('a.L_Act_A_id', 'asc')
                ->select(
                    'a.L_Act_A_id',
                    'a.L_Act_St_date',
                    'a.Pl_Sten_id',
                    'a.A_Cm_Stid',
                    'c.A_ns_C_id',
                    'c.A_ns_CL_value',
                    's1.L_Act_St_name',
                    's2.L_Act_St2_name',

                )

                ->get();

            $act->proposal_12_list = DB::table('L_Acts_Proposal as s')
                ->where('s.L_Act_id', $id)
                ->where('s.C_St_id', 1)
                ->whereRaw('LENGTH(ANOT) > 29')
                ->get();

            foreach ($act->proposal_12_list as $key_parent1 => $pr12) {
                $pr_imp_list = DB::table('L_Act_Pr_Importer as li')
                    ->join('A_ns_Mps_Lng as mpl4', function ($join) use ($lng) {
                        $join->on('mpl4.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                            ->where('mpl4.C_Lang_id', '=',  $lng);
                    })
                    ->where('li.ID_DOC', $pr12->ID)
                    ->orderBy('li.ID', 'asc')
                    ->select('mpl4.A_ns_MP_id', 'mpl4.A_ns_MPL_Name1', 'mpl4.A_ns_MPL_Name2', 'mpl4.A_ns_MPL_Name3')
                    ->get();

                $act_files = DB::table('L_Act_Files')
                    ->where('L_Act_id', $act->sync_ID)
                    ->where('VID_DOC', 1555)
                    ->where('FILENAME', 'like', "%{$pr12->RN_DOC}.%")
                    // ->where('C_St_id', '!=', 2)
                    ->where(function ($query) {
                        $query->whereNull('C_St_id')
                            ->orWhere('C_St_id',  1);
                    })
                    ->select('FILENAME')
                    ->get();

                $act->proposal_12_list[$key_parent1]->imp_list = $pr_imp_list;
                $act->proposal_12_list[$key_parent1]->act_files = $act_files;

                // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
            }
        }





        $response = Response::json($act, 200);
        return $response;

        // return $actList;
    }



    public function actPrProfile($id)
    {
        // return 1;

        $lng  = 1;
        $act = DB::table('L_Acts as t1')
            ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                    ->where('t2.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions_Lng as s1', function ($join) use ($lng) {
                $join->on('s1.L_Ses_id', '=', 't1.L_Ses_id')
                    ->where('s1.C_Lang_id', '=',  $lng);
            })
            ->join('L_Act_Type as tp1', 'tp1.L_Act_T_id', '=', 't1.L_Act_T_id')
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 't1.L_Ses_id')
            ->join('A_ns_Assembly as s3', 's3.A_ns_id', '=', 's.A_ns_id')
            ->where('t1.L_Act_id', $id)
            // ->where('t1.C_St_id', 1)
            ->whereRaw('LENGTH(t2.L_ActL_title) > 29')
            ->where('t1.L_Act_T_id', '!=', 1)
            ->select(
                't1.L_Act_id',
                'tp1.L_Act_T_name',
                't1.L_Act_sign',
                't1.L_Act_new_id',
                't1.L_Act_date',
                't1.L_Act_T_id',
                't1.L_UT',
                't1.L_act_proposal_date',
                't1.L_act_proposal_date2',
                't1.sync_ID',
                't1.L_Act_dv_ID',
                't1.L_Act_date2',
                't1.L_Act_dv_iss',
                't1.L_Act_dv_year',
                't2.L_ActL_title',
                't2.L_ActL_imp',
                't2.L_ActL_final',
                's3.A_ns_folder',
                's1.L_SesL_value',
            );



        $act = $act->first();


        if ($act) {

            if ($act->L_Act_T_id == 2) {
                $Vid_doc = 1550;
            } elseif ($act->L_Act_T_id == 4) {
                $Vid_doc = 1607;
            } else {
                $Vid_doc = 1560;
            }




            $act->file_list = DB::table('L_Act_Files')

                ->where('L_Act_id', $act->sync_ID)
                ->where('VID_DOC', $Vid_doc)
                ->where(function ($query) {
                    $query->whereNull('C_St_id')
                        ->orWhere('C_St_id',  1);
                })
                // ->where('C_St_id', '!=', 2)
                ->where('FILENAME', 'like', "%{$act->L_Act_sign}%")
                ->select('FILENAME', 'ID')
                ->get();



            $act->imp_list = DB::table('L_Acts_Importer as li')
                ->join('A_ns_Mps_Lng as mpl2', function ($join) use ($lng) {
                    $join->on('mpl2.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                        ->where('mpl2.C_Lang_id', '=',  $lng);
                })
                ->where('li.L_Act_id', $id)
                ->orderBy('li.sync_ID', 'asc')
                ->select('mpl2.A_ns_MP_id', 'mpl2.A_ns_MPL_Name1', 'mpl2.A_ns_MPL_Name2', 'mpl2.A_ns_MPL_Name3')
                ->get();

            $act->imp_list_min = DB::table('ZD_ACT_Vnositeli as li')

                ->where('li.ID_ACT', $id)
                ->where('li.VNOSITEL', 6167)
                ->select('li.ID_ACT')
                ->get();

            if ($act->L_UT) {
                $act->dist_list = DB::table('L_Act_Distr as d')
                    ->join('A_ns_Coll_Lng as c', function ($join) use ($lng) {
                        $join->on('c.A_ns_C_id', '=', 'd.A_ns_C_id')
                            ->where('c.C_Lang_id', '=',  $lng);
                    })
                    ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 'd.A_ns_C_id')
                    ->join('L_Act_DT_Distr_Type_Lng as dt', function ($join) use ($lng) {
                        $join->on('dt.L_Act_DT_id', '=', 'd.L_Act_DT_id')
                            ->where('dt.C_Lang_id', '=',  $lng);
                    })
                    ->where('d.L_Act_id', $id)
                    ->orderBy('dt.L_Act_DTL_id', 'asc')
                    ->orderBy('d.sync_ID', 'asc')
                    ->select('d.L_Act_D_id', 'c.A_ns_C_id', 'c.A_ns_CL_value', 'dt.L_Act_DTL_value')
                    ->get();
            }

            $act->proposal_12_list = DB::table('L_Acts_Proposal as s')
                ->where('s.L_Act_id', $id)
                ->where('s.C_St_id', 1)
                ->where('RN_DOC', '!=', $act->L_Act_sign)
                ->whereRaw('LENGTH(ANOT) > 29')
                ->get();

            foreach ($act->proposal_12_list as $key_parent1 => $pr12) {
                $pr_imp_list = DB::table('L_Act_Pr_Importer as li')
                    ->join('A_ns_Mps_Lng as mpl5', function ($join) use ($lng) {
                        $join->on('mpl5.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                            ->where('mpl5.C_Lang_id', '=',  $lng);
                    })
                    ->where('li.ID_DOC', $pr12->ID)
                    ->orderBy('li.ID', 'asc')
                    ->select('mpl5.A_ns_MP_id', 'mpl5.A_ns_MPL_Name1', 'mpl5.A_ns_MPL_Name2', 'mpl5.A_ns_MPL_Name3')
                    ->get();

                $act_files = DB::table('L_Act_Files')
                    ->where('L_Act_id', $act->sync_ID)
                    ->where('VID_DOC', 35374)
                    ->where('FILENAME', 'like', "%{$pr12->RN_DOC}%")
                    // ->where('C_St_id', '!=', 2)
                    ->where(function ($query) {
                        $query->whereNull('C_St_id')
                            ->orWhere('C_St_id',  1);
                    })
                    ->select('FILENAME')
                    ->get();

                $act->proposal_12_list[$key_parent1]->imp_list = $pr_imp_list;
                $act->proposal_12_list[$key_parent1]->act_files = $act_files;
            }
        }



        $act->latest_status = DB::table('L_Act_Activity as a')

            ->join('L_Act_Status as s1', 's1.L_Act_St_id', '=', 'a.L_Act_St_id')
            ->join('L_Act_Status2 as s2', 's2.L_Act_St2_id', '=', 'a.L_Act_St2_id')

            ->where('a.L_Act_id', $id)
            ->select('a.L_Act_St_date', 's2.L_Act_St2_name')
            ->orderby('L_Act_St_date', 'desc')
            ->first();





        $response = Response::json($act, 200);
        return $response;

        // return $actList;
    }


    public function actProfile($id)
    {
        // return 1;

        $lng  = 1;
        $act = DB::table('L_Acts as t1')
            ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
                $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
                    ->where('t2.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions_Lng as s1', function ($join) use ($lng) {
                $join->on('s1.L_Ses_id', '=', 't1.L_Ses_id')
                    ->where('s1.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 't1.L_Ses_id')
            ->join('A_ns_Assembly as s3', 's3.A_ns_id', '=', 's.A_ns_id')
            ->where('t1.L_Act_id', $id)
            // ->where('t1.C_St_id', 1)
            // ->where('t1.L_Act_T_id', '!=', 1)
            ->select(
                't1.L_Act_id',
                't1.L_Act_T_id',
                't1.L_Act_sign',
                't1.L_Act_new_id',
                't1.L_Act_date',
                't1.L_act_proposal_date',
                't1.L_act_proposal_date2',
                't1.sync_ID',
                't1.L_Act_dv_ID',
                't1.L_Act_date2',
                't1.L_Act_dv_iss',
                't1.L_Act_dv_year',
                't2.*',

                's3.A_ns_folder',
            );



        $act = $act->first();


        // $act->union_list = DB::table('L_Acts as t1')
        //     ->join('L_Acts_Lng as t2', function ($join) use ($lng) {
        //         $join->on('t2.L_Act_id', '=', 't1.L_Act_id')
        //             ->where('t2.C_Lang_id', '=',  $lng);
        //     })
        //     ->where('t1.L_Act_new_id', $id)
        //     ->select('t1.L_Act_id', 't2.L_ActL_title')
        //     ->get();








        $response = Response::json($act, 200);
        return $response;

        // return $actList;
    }



    public function fnBills(Request $request)
    {

        $lng = 1;
        if (isset($request->L_Ses_id)) {
            $L_Ses_id =  (int) $request->L_Ses_id["id"];
        } else {
            $L_Ses_id =  null;
        }

        if (isset($request->A_ns_C_id)) {
            $A_ns_C_id =  (int) $request->A_ns_C_id["id"];
        } else {
            $A_ns_C_id =  null;
        }
        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
        }

        // return $Pr_id;

        if (isset($request->A_ns_MP_id)) {
            $A_ns_MP_id =  (int) $request->A_ns_MP_id["id"];
        } else {
            $A_ns_MP_id =  null;
        }
        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }

        $L_Act_string = cleanUp($request->L_Act_string);
        $L_Act_sign = cleanUp($request->L_Act_sign);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        // if (!$A_ns_id) {
        //     $A_ns_id = currentNS();
        // }

        $funnel = DB::table('L_Acts as l')
            ->join('L_Acts_Lng as l18n', function ($join) use ($lng) {
                $join->on('l18n.L_Act_id', '=', 'l.L_Act_id')
                    ->where('l18n.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 'l.L_Ses_id');



        // ->leftjoin('L_Acts_Lng as l18n', function ($join) {
        //     $join->on('l18n.L_Act_id', '=', 'V.L_Act_id')
        //         ->where('l18n.C_Lng_id', '=', 1);
        // });

        $funnel->select(
            'l.L_Act_date',
            'l.L_Act_sign',
            'l.L_Act_id',
            'l18n.L_ActL_title',


        );

        if ($date1) {
            $funnel->where('l.L_Act_date', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('l.L_Act_date', '<=', $date2);
        }

        if ($Pr_id) {
            if ($Pr_id == 'week') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {
                $funnel->where('s.A_ns_id', currentNS());
            }
        }
        if ($L_Act_sign) {
            $funnel->where('l.L_Act_sign', 'like', "{$L_Act_sign}%");
        }
        if ($L_Act_string) {
            $funnel->where('l18n.L_ActL_title', 'like', '%' . $L_Act_string . '%');
        }
        if ($L_Ses_id) {
            $funnel->where('l.L_Ses_id', '=', $L_Ses_id);
        }
        if ($A_ns_id) {
            $funnel->where('s.A_ns_id', '=', $A_ns_id);
        }

        if ($A_ns_C_id) {
            $funnel->join('L_Act_Distr as d', 'd.L_Act_id', '=', 'l.L_Act_id')
                ->where('d.A_ns_C_id', '=', $A_ns_C_id);
        }

        if ($A_ns_MP_id) {
            $funnel->join('L_Acts_Importer as im', 'im.L_Act_id', '=', 'l.L_Act_id')
                ->where('im.A_ns_MP_id', '=', $A_ns_MP_id);
        }

        if ($request->impMin) {
            $funnel->join('L_Acts_Importer as im1', 'im1.L_Act_id', '=', 'l.L_Act_id')
                ->where('im1.A_ns_C_id', '=', 6167);
        }


        if (!$request->search) {
            $funnel->where('s.A_ns_id', currentNS());
        }



        $funnel->where('l.L_Act_T_id', 1)
            // ->where('l.C_St_id', 1)
            // ->where('s.A_ns_id', $A_ns_id)
            ->whereRaw('LENGTH(l18n.L_ActL_title) > 29');
        $funnel->orderBy('l.L_Act_date', 'desc');

        $funnel = $funnel->get();


        foreach ($funnel as $key_parent1 => $fn) {
            $imp_list = DB::table('L_Acts_Importer as li')
                ->join('A_ns_Mps_Lng as mpl_6', function ($join) use ($lng) {
                    $join->on('mpl_6.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                        ->where('mpl_6.C_Lang_id', '=',  $lng);
                })
                ->where('li.L_Act_id', $fn->L_Act_id)
                ->orderBy('li.sync_ID', 'asc')
                ->select('li.L_Act_im_id', 'mpl_6.A_ns_MP_id', 'mpl_6.A_ns_MPL_Name1', 'mpl_6.A_ns_MPL_Name2', 'mpl_6.A_ns_MPL_Name3')
                ->get();

            $imp_list_min = DB::table('L_Acts_Importer as li')

                ->where('li.L_Act_id', $fn->L_Act_id)
                ->where('li.A_ns_C_id', 6167)
                ->select('li.L_Act_im_id', 'li.A_ns_C_id')
                ->get();


            $funnel[$key_parent1]->imp_list = $imp_list;

            $funnel[$key_parent1]->imp_list_min = $imp_list_min;

            // echo L_Act_Files ($row["sync_ID"], 1555, $row["A_ns_folder"], $row1["RN_DOC"]); //
        }





        $response = Response::json($funnel, 200);
        return $response;
    }


    public function fnActPr(Request $request)
    {
        // return $request->L_Ses_id["id"];
        $lng = 1;
        // $L_Ses_id = (int)$request->L_Ses_id["id"];
        if (isset($request->L_Ses_id)) {
            $L_Ses_id =  (int) $request->L_Ses_id["id"];
        } else {
            $L_Ses_id =  null;
        }



        if (isset($request->L_Act_T_id)) {
            $L_Act_T_id =  (int) $request->L_Act_T_id["id"];
        } else {
            $L_Act_T_id =  null;
        }
        if (isset($request->A_ns_MP_id)) {
            $A_ns_MP_id =  (int) $request->A_ns_MP_id["id"];
        } else {
            $A_ns_MP_id =  null;
        }
        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }

        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
        }

        $L_Act_string = cleanUp($request->L_Act_string);
        $L_Act_sign = cleanUp($request->L_Act_sign);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        // if (!$A_ns_id) {
        //     $A_ns_id = currentNS();
        // }

        $funnel = DB::table('L_Acts as l')
            ->join('L_Acts_Lng as l18n', function ($join) use ($lng) {
                $join->on('l18n.L_Act_id', '=', 'l.L_Act_id')
                    ->where('l18n.C_Lang_id', '=',  $lng);
            })

            ->join('L_Act_Type as tp1', 'tp1.L_Act_T_id', '=', 'l.L_Act_T_id')
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 'l.L_Ses_id');


        $funnel->select(
            'l.L_Act_date',
            'l.L_Act_sign',
            'l.L_Act_id',
            'l.L_Act_T_id',
            'tp1.L_Act_T_name',
            'l18n.L_ActL_title',
            'l18n.L_ActL_final',
            'l18n.L_ActL_imp',


        );

        if ($date1) {
            $funnel->where('l.L_Act_date', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('l.L_Act_date', '<=', $date2);
        }
        if ($Pr_id) {
            if ($Pr_id == 'week') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('l.L_Act_date', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {
                $funnel->where('s.A_ns_id', currentNS());
            }
        }
        if ($A_ns_id) {
            $funnel->where('s.A_ns_id', '=', $A_ns_id);
        }
        if ($L_Act_sign) {
            $funnel->where('l.L_Act_sign', 'like', "{$L_Act_sign}%");
        }
        if ($L_Act_string) {
            $funnel->where('l18n.L_ActL_title', 'like', '%' . $L_Act_string . '%');
        }
        if ($L_Ses_id) {
            $funnel->where('l.L_Ses_id', '=', $L_Ses_id);
        }

        if ($L_Act_T_id) {
            $funnel->where('l.L_Act_T_id', '=', $L_Act_T_id);
        }

        if ($A_ns_MP_id) {
            $funnel->join('L_Acts_Importer as im', 'im.L_Act_id', '=', 'l.L_Act_id')
                ->where('im.A_ns_MP_id', '=', $A_ns_MP_id);
        }


        if (!$request->search) {
            $funnel->where('s.A_ns_id', currentNS());
        }
        $funnel->where('l.L_Act_T_id', '!=', 1)
            // ->where('l.C_St_id', 1)
            // ->where('s.A_ns_id', $A_ns_id)
            ->whereRaw('LENGTH(l18n.L_ActL_title) > 29');
        $funnel->orderBy('l.L_Act_date', 'desc');
        // if ($request->search) {
        //     $funnel->take(800);
        // } else {

        //     $funnel->take(30);
        // }
        $funnel = $funnel->get();


        foreach ($funnel as $key_parent1 => $fn) {
            $imp_list = DB::table('L_Acts_Importer as li')
                ->join('A_ns_Mps_Lng as mpl_6', function ($join) use ($lng) {
                    $join->on('mpl_6.A_ns_MP_id', '=', 'li.A_ns_MP_id')
                        ->where('mpl_6.C_Lang_id', '=',  $lng);
                })
                ->where('li.L_Act_id', $fn->L_Act_id)
                ->orderBy('li.sync_ID', 'asc')
                ->select('li.L_Act_im_id', 'mpl_6.A_ns_MP_id', 'mpl_6.A_ns_MPL_Name1', 'mpl_6.A_ns_MPL_Name2', 'mpl_6.A_ns_MPL_Name3')
                ->get();




            $funnel[$key_parent1]->imp_list = $imp_list;
        }





        $response = Response::json($funnel, 200);
        return $response;
    }

    public function fnActs($actType, Request $request)
    {
        // return $request->L_Ses_id["id"];
        $lng = 1;
        // $L_Ses_id = (int)$request->L_Ses_id["id"];
        if (isset($request->L_Ses_id)) {
            $L_Ses_id =  (int) $request->L_Ses_id["id"];
        } else {
            $L_Ses_id =  null;
        }


        $L_Act_T_id =  (int)$actType;


        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }
        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
        }

        $L_Act_string = cleanUp($request->L_Act_string);
        $L_Act_sign = cleanUp($request->L_Act_sign);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        // if (!$A_ns_id) {
        //     $A_ns_id = currentNS();
        // }

        $funnel = DB::table('L_Acts as l')
            ->join('L_Acts_Lng as l18n', function ($join) use ($lng) {
                $join->on('l18n.L_Act_id', '=', 'l.L_Act_id')
                    ->where('l18n.C_Lang_id', '=',  $lng);
            })
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 'l.L_Ses_id');


        $funnel->select(
            'l.L_Act_date',
            'l.L_Act_date2',
            'l.L_Act_dv_iss',
            'l.L_Act_dv_year',
            'l.L_Act_sign',
            'l.L_Act_id',
            'l18n.L_ActL_final',


        );

        if ($date1) {
            $funnel->where('l.L_Act_date2', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('l.L_Act_date2', '<=', $date2);
        }
        if ($Pr_id) {
            if ($Pr_id == 'week') {
                $funnel->whereBetween('l.L_Act_date2', [Carbon::now()->startOfWeek(), Carbon::now()->endOfWeek()]);
            } elseif ($Pr_id == 'month') {
                $funnel->whereBetween('l.L_Act_date2', [Carbon::now()->startOfMonth(), Carbon::now()->endOfMonth()]);
            } elseif ($Pr_id == 'year') {
                $funnel->whereBetween('l.L_Act_date2', [Carbon::now()->startOfYear(), Carbon::now()->endOfYear()]);
            } elseif ($Pr_id == 'currentNs') {
                $funnel->where('s.A_ns_id', currentNS());
            }
        }
        if ($L_Act_sign) {
            $funnel->where('l.L_Act_sign', 'like', "{$L_Act_sign}%");
        }
        if ($L_Act_string) {
            $funnel->where('l18n.L_ActL_final', 'like', '%' . $L_Act_string . '%');
        }
        if ($L_Ses_id) {
            $funnel->where('l.L_Ses_id', '=', $L_Ses_id);
        }
        if ($A_ns_id) {
            $funnel->where('s.A_ns_id', '=', $A_ns_id);
        }


        $funnel->where('l.L_Act_T_id', '=', $L_Act_T_id);
        if (!$request->search) {
            $funnel->where('s.A_ns_id', currentNS());
        }

        $funnel->where('l.L_Act_dv_ID', '>', 0)
            // ->where('l.C_St_id', 1)
        ;
        // ->where('s.A_ns_id', $A_ns_id)
        $funnel->orderBy('l.L_Act_date', 'desc');
        // if ($request->search) {
        //     $funnel->take(300);
        // } else {

        //     $funnel->take(30);
        // }
        $funnel = $funnel->get();





        $response = Response::json($funnel, 200);
        return $response;
    }


    public function actDiscuss(Request $request)
    {

        $validator = FacadesValidator::make($request->all(), [
            'L_ActDs_author' => 'required',
            'L_ActDs_email' => 'required|email',
            'L_ActDs_body' => 'required',
        ]);

        $actExists = DB::table('L_Acts')
            ->where('L_Act_id', $request->actId)
            ->exists();

        if ($validator->fails() || !$actExists) {
            // $response = Response::json([
            //     'error' => 'Непълни данни'
            // ], 422);

            $response = Response::json([
                'actId' => $request->actId,
                'status' => false,
                'message' => 'Непълни данни',

            ], 200);

            return $response;
        }







        $discuss = new LActDiscuss(array(
            'L_Act_id' => trim($request->actId),
            'L_ActDs_author' => trim($request->L_ActDs_author),
            'L_ActDs_email' => trim($request->L_ActDs_email),
            'L_ActDs_topic' => trim($request->L_ActDs_topic),
            'L_ActDs_body' => trim($request->L_ActDs_body),


        ));
        $discuss->save();


        $response = Response::json([
            'actId' => $request->actId,
            'status' => true,
            'message' => 'Успешно записана информация',

        ], 200);


        return $response;
    }


    public function consultBills(Request $request)
    {

        $lng = 1;
        // $L_Ses_id = (int)$request->L_Ses_id["id"];
        if (isset($request->L_Ses_id)) {
            $L_Ses_id =  (int) $request->L_Ses_id["id"];
        } else {
            $L_Ses_id =  null;
        }

        if (isset($request->A_ns_C_id)) {
            $A_ns_C_id =  (int) $request->A_ns_C_id["id"];
        } else {
            $A_ns_C_id =  null;
        }
        if (isset($request->Pr_id)) {
            $Pr_id =  $request->Pr_id["id"];
        } else {
            $Pr_id =  null;
        }

        // return $Pr_id;

        if (isset($request->A_ns_MP_id)) {
            $A_ns_MP_id =  (int) $request->A_ns_MP_id["id"];
        } else {
            $A_ns_MP_id =  null;
        }
        if (isset($request->A_ns_id)) {
            $A_ns_id =  (int) $request->A_ns_id["id"];
        } else {
            $A_ns_id =  null;
        }

        $L_Act_string = cleanUp($request->L_Act_string);
        $L_Act_sign = cleanUp($request->L_Act_sign);
        $date1 = cleanUp($request->date1);
        $date2 = cleanUp($request->date2);
        // if (!$A_ns_id) {
        //     $A_ns_id = currentNS();
        // }

        $funnel = DB::table('L_Acts as l')
            ->join('L_Acts_Lng as l18n', function ($join) use ($lng) {
                $join->on('l18n.L_Act_id', '=', 'l.L_Act_id')
                    ->where('l18n.C_Lang_id', '=',  $lng);
            })
            // ->leftjoin('L_Act_Discuss as d', 'd.L_Act_id', '=', 'l.L_Act_id')
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 'l.L_Ses_id');


        $funnel->select(
            'l.L_Act_date',
            'l.L_Act_sign',
            'l.L_Act_id',
            'l18n.L_ActL_title',
            DB::raw('(SELECT COUNT(*) FROM L_Act_Discuss as d WHERE d.L_Act_id = l.L_Act_id and d.C_St_id=1) as conCount')


        );

        if ($date1) {
            $funnel->where('l.L_Act_date', '>=', $date1);
        }
        if ($date2) {
            $funnel->where('l.L_Act_date', '<=', $date2);
        }


        if ($L_Act_sign) {
            $funnel->where('l.L_Act_sign', 'like', "{$L_Act_sign}%");
        }
        if ($L_Act_string) {
            $funnel->where('l18n.L_ActL_title', 'like', '%' . $L_Act_string . '%');
        }
        if ($L_Ses_id) {
            $funnel->where('l.L_Ses_id', '=', $L_Ses_id);
        }
        if ($A_ns_id) {
            $funnel->where('s.A_ns_id', '=', $A_ns_id);
        }

        if ($A_ns_C_id) {
            $funnel->join('L_Act_Distr as d', 'd.L_Act_id', '=', 'l.L_Act_id')
                ->where('d.A_ns_C_id', '=', $A_ns_C_id);
        }

        if ($A_ns_MP_id) {
            $funnel->join('L_Acts_Importer as im', 'im.L_Act_id', '=', 'l.L_Act_id')
                ->where('im.A_ns_MP_id', '=', $A_ns_MP_id);
        }


        // $funnel->join('L_Acts_Importer as im1', 'im1.L_Act_id', '=', 'l.L_Act_id')
        //     ->where('im1.A_ns_C_id', '!=', 6167);

        $funnel->where('s.A_ns_id', currentNS());




        $funnel->where('l.L_Act_T_id', 1)
            // ->where('l.C_St_id', 1)
            // ->where('s.A_ns_id', $A_ns_id)
            ->whereRaw('LENGTH(l18n.L_ActL_title) > 29');
        $funnel->orderBy('l.L_Act_date', 'desc');
        // if ($request->search) {
        //     $funnel->take(1000);
        // } else {

        //     $funnel->take(30);
        // }
        $funnel = $funnel->get();





        $response = Response::json($funnel, 200);
        return $response;
    }


    public function consultDiscuss($id)
    {

        $lng = 1;

        $profile = DB::table('L_Acts as l')
            ->join('L_Acts_Lng as l18n', function ($join) use ($lng) {
                $join->on('l18n.L_Act_id', '=', 'l.L_Act_id')
                    ->where('l18n.C_Lang_id', '=',  $lng);
            })
            // ->leftjoin('L_Act_Discuss as d', 'd.L_Act_id', '=', 'l.L_Act_id')
            ->join('L_Sessions as s', 's.L_Ses_id', '=', 'l.L_Ses_id');


        $profile->select(
            'l.L_Act_date',
            'l.L_Act_sign',
            'l.L_Act_id',
            'l18n.L_ActL_title',
            // DB::raw('count(d.*) as ddd')
            DB::raw('(SELECT COUNT(*) FROM L_Act_Discuss as d WHERE d.L_Act_id = l.L_Act_id and d.C_St_id=1) as conCount')


        );

        // $funnel->join('L_Acts_Importer as im1', 'im1.L_Act_id', '=', 'l.L_Act_id')
        //     ->where('im1.A_ns_C_id', '!=', 6167);

        $profile->where('s.A_ns_id', currentNS());




        $profile->where('l.L_Act_T_id', 1)
            // ->where('l.C_St_id', 1)
            ->where('l.L_Act_id', $id)
            // ->where('s.A_ns_id', $A_ns_id)
            ->whereRaw('LENGTH(l18n.L_ActL_title) > 29');

        $profile = $profile->first();


        if ($profile) {
            $profile->discuss = DB::table('L_Act_Discuss')
                ->where('L_Act_id', $id)
                ->where('C_St_id', 1)
                ->whereNull('deleted_at')
                ->orderBy('created_at', 'desc')
                ->select('L_ActDs_id', 'L_ActDs_author', 'L_ActDs_email', 'L_ActDs_topic', 'L_ActDs_body', 'created_at')
                ->get();

            // $profile->discuss->L_ActDs_body = nl2br($profile->discuss->L_ActDs_body);
        }




        $response = Response::json($profile, 200);
        return $response;
    }
}
