<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class NSController extends Controller
{
    public function colList($lng, $type)
    {
        // return 1;

        $lng =  convert18n($lng);


        $colList = DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->where('cl.A_ns_CT_id', $type)
            ->where('cl.C_St_id', 1)
            ->where('cl.A_ns_id', currentNS())
            ->select(
                'cl.A_ns_C_id',
                'cl.A_ns_CT_id',
                'c18n.A_ns_CL_value',
                'cl.A_ns_C_active_count',
                'cl.A_ns_C_date_F',
                'cl.A_ns_C_date_T',

            )
            // ->orderBy('cl.A_ns_C_order', 'desc')
            // ->orderBy('cl.A_ns_C_active_count', 'desc');
        ;
        // 



        if ($type == 2) {
            $colList->orderBy('cl.A_ns_C_order', 'desc')
                ->orderBy('cl.A_ns_C_active_count', 'desc')
                ->orderBy('c18n.A_ns_CL_value', 'asc');
            // $sql.=" ORDER BY C.A_ns_C_order desc, CNT DESC,  CL.A_ns_CL_value ASC";
        } elseif ($type == 3) {
            $colList->orderBy('cl.A_ns_C_order', 'desc')
                ->orderBy('cl.A_ns_C_id', 'asc');
            // $sql.=" ORDER BY C.A_ns_C_order desc, C.A_ns_C_id ASC";
        } elseif ($type == 9) {
            $colList->orderBy('cl.A_ns_C_id', 'asc');
            // $sql.=" ORDER BY C.A_ns_C_id ASC";
        } elseif ($type == 4) {
            $colList->orderBy('cl.A_ns_C_id', 'asc');
            // $sql.=" ORDER BY C.A_ns_C_id ASC";
        } elseif ($type == 11) {
            $colList->orderBy('cl.A_ns_C_id', 'asc');
            // $sql.=" ORDER BY CL.A_ns_CL_value ASC"; 
        } elseif ($type == 14) {
            $colList->orderBy('cl.A_ns_C_id', 'asc');
        }

        $colList = $colList->get();

        $response = Response::json($colList, 200);
        return $response;
    }

    public function ColListPresident($lng)
    {

        $lng = convert18n($lng);

        $colId = currentNSCID();
        // return $this->endNS();

        if ($this->endNS()) {
            $endDate = '2000-01-01';
        } else {
            $endDate = Carbon::now();
        }

        if (!DB::table('A_ns_Coll as cl')
            ->where('cl.A_ns_C_id', $colId)
            // ->where('cl.A_ns_CT_id', $type)
            ->exists()) {
            $response = Response::json([
                'status' => false,
                'message' => 'Record not found',
            ], 404);

            return $response;
        }
        $colData = DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->where('cl.A_ns_C_id', $colId)
            ->select(
                'cl.A_ns_C_id',
                'c18n.A_ns_CL_value',
                'cl.A_ns_C_active_count',

            )
            ->first();

        // return 1;
        $colData->colListMP = DB::table('A_ns_MShip as s')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 's.A_ns_MP_id')
            // ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 's.A_ns_C_id')
            ->join('A_ns_MP_Pos as p', 'p.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            ->join('A_ns_Mps_Lng as mpl_9', function ($join) use ($lng) {
                $join->on('mpl_9.A_ns_MP_id', '=', 's.A_ns_MP_id')
                    ->where('mpl_9.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Va as va', 'va.A_ns_Va_id', '=', 'mp.A_ns_Va_id')
            ->where('s.A_ns_C_id', $colId)
            ->where(function ($query) {
                $query->where('s.A_ns_MP_Pos_id', '=', '2')
                    ->orWhere('s.A_ns_MP_Pos_id', '=', '3');
            })
            ->where('mp.C_St_id', 1)
            ->where('s.A_ns_MSP_date_T', '>=', $endDate)
            ->where('s.A_ns_MSP_date_F', '<=',  Carbon::now())
            ->select(
                // 'cl.A_ns_C_id',
                // 'cl.A_ns_C_active_cnt',
                // 'c18n.A_ns_CL_value_short',
                // 'c18n.A_ns_CL_value',
                's.A_ns_MSP_date_F',
                's.A_ns_MSP_date_T',
                'mp.A_ns_MP_FM',
                'mp.A_ns_MP_id',
                // 'mp.A_ns_MP_BDate',
                // 'mp.A_ns_B_Country',
                // 'mp.A_ns_MP_fbook',
                // 'mp.A_ns_MP_Email',
                // 'mp.A_ns_MP_url',
                'mp.A_ns_MP_leg_count',
                'mp.A_ns_MP_com_count',
                'mp.A_ns_MP_del_count',
                'mp.A_ns_MP_frd_count',
                'mpl_9.A_ns_MPL_Name1',
                'mpl_9.A_ns_MPL_Name2',
                'mpl_9.A_ns_MPL_Name3',
                'pl.A_ns_MP_PosL_value',
                'va.A_ns_Va_name'

            )
            ->orderBy('p.A_ns_MP_Pos_prior', 'desc')
            ->orderBy('mpl_9.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name3', 'asc')
            ->get();






        $response = Response::json($colData, 200);
        return $response;
    }


    public function ColListNS($lng, Request $request)
    {
        // return $request;

        $lng = convert18n($lng);
        if ($request->nsId) {
            // $colId = $request->nsId;

            $colId  = DB::table('A_ns_Coll')
                ->where('A_ns_CT_id', 1)
                ->where('A_ns_id', $request->nsId)
                ->select(
                    'A_ns_C_id'
                )
                ->first();

            if ($colId) {
                $colId = $colId->A_ns_C_id;
            }
        } else {
            $colId = currentNSCID();
        }










        // При конфигурация на закрито НС и все още не е активиран архив
        if ($this->endNS() and  $this->collectionCurrentNS(currentNSCID())) {
            $date = '9999-12-31';
            $date1 = '2000-01-01';
        } else {

            if ($request->date) {

                $date = $request->date;
                $date1 = $request->date;
            } else {
                $date = date("Y-m-d");
                $date1 = date("Y-m-d");
                // $date = Carbon::now();
                // $date1 = Carbon::now();
            }
        }



        if (!DB::table('A_ns_Coll as cl')
            ->where('cl.A_ns_C_id', $colId)
            // ->where('cl.A_ns_CT_id', $type)
            ->exists()) {
            $response = Response::json([
                'status' => false,
                'message' => 'Record not found',
            ], 404);

            return $response;
        }
        $colData = DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->where('cl.A_ns_C_id', $colId)
            ->select(
                'cl.A_ns_C_id',
                'c18n.A_ns_CL_value',
                'c18n.A_ns_CL_value_short',
                'cl.A_ns_C_active_count',

            )
            ->first();

        // return 1;
        $colListMP = DB::table('A_ns_MShip as s')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 's.A_ns_MP_id')
            ->join('A_ns_MP_Pos as p', 'p.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            ->join('A_ns_Mps_Lng as mpl_9', function ($join) use ($lng) {
                $join->on('mpl_9.A_ns_MP_id', '=', 's.A_ns_MP_id')
                    ->where('mpl_9.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->where('mp.C_St_id', 1)
            ->where('s.A_ns_C_id', $colId)
            ->join('A_ns_MShip as s1', 's1.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
            ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 's1.A_ns_C_id')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 's1.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Va as va', 'va.A_ns_Va_id', '=', 'mp.A_ns_Va_id')
            ->join('A_ns_MP_Pos_Lng as pl1', function ($join) use ($lng) {
                $join->on('pl1.A_ns_MP_Pos_id', '=', 's1.A_ns_MP_Pos_id')
                    ->where('pl1.C_Lang_id', '=',  $lng);
            });
        if ($date != '9999-12-31') {
            $colListMP->where('s.A_ns_MSP_date_T', '>', $date)
                ->where('s.A_ns_MSP_date_F', '<=', $date)
                ->where('s1.A_ns_MSP_date_T', '>=', $date)
                ->where('s1.A_ns_MSP_date_F', '<=', $date);
        }
        $colListMP->where('cl.A_ns_CT_id', 2);


        if ($request->munId) {
            $colListMP->join('A_ns_Va_Mun as mn', 'mn.A_ns_Va_id', '=', 'va.A_ns_Va_id')
                ->where('mn.A_ns_Va_M_id', $request->munId);
        }

        if ($request->pgId) {
            $colListMP->join('A_ns_MShip as s2', 's2.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                ->where('s2.A_ns_C_id', $request->pgId)
                ->where('s2.A_ns_MSP_date_T', '>=', $date1)
                ->where('s2.A_ns_MSP_date_F', '<=', Carbon::now());
        }

        if ($request->vaId) {
            $colListMP->where('mp.A_ns_Va_id', $request->vaId);
        }

        if ($request->coalId) {
            $colListMP->where('mp.A_ns_Coal_id', $request->coalId);
        }

        if ($request->A_ns_MPL_Name1) {
            $colListMP->where('mpl_9.A_ns_MPL_Name1', 'like', "{$request->A_ns_MPL_Name1}%");
        }
        if ($request->A_ns_MPL_Name2) {
            $colListMP->where('mpl_9.A_ns_MPL_Name2', 'like', "{$request->A_ns_MPL_Name2}%");
        }
        if ($request->A_ns_MPL_Name3) {
            $colListMP->where('mpl_9.A_ns_MPL_Name3', 'like', "{$request->A_ns_MPL_Name3}%");
        }

        $colListMP->select(
            'cl.A_ns_C_id',
            // 'cl.A_ns_C_active_cnt',
            // 'c18n.A_ns_CL_value',
            's.A_ns_MSP_date_F',
            's.A_ns_MSP_date_T',
            'mp.A_ns_MP_FM',
            'mp.A_ns_MP_id',
            'c18n.A_ns_CL_value',
            'c18n.A_ns_CL_value_short',
            // 'mp.A_ns_MP_BDate',
            // 'mp.A_ns_B_Country',
            // 'mp.A_ns_MP_fbook',
            // 'mp.A_ns_MP_Email',
            // 'mp.A_ns_MP_url',
            'mp.A_ns_MP_leg_count',
            'mp.A_ns_MP_com_count',
            'mp.A_ns_MP_del_count',
            'mp.A_ns_MP_frd_count',
            'mpl_9.A_ns_MPL_Name1',
            'mpl_9.A_ns_MPL_Name2',
            'mpl_9.A_ns_MPL_Name3',
            'pl.A_ns_MP_Pos_id',
            'pl.A_ns_MP_PosL_value',
            'pl1.A_ns_MP_PosL_value as A_ns_MP_PosL_value1',
            'va.A_ns_Va_name'

        )
            ->groupBy('mp.A_ns_MP_id')
            ->orderBy('p.A_ns_MP_Pos_prior', 'desc')
            ->orderBy('mpl_9.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name3', 'asc');


        $colData->colListMP = $colListMP->get();






        $response = Response::json($colData, 200);
        return $response;
    }




    public function ColListMP($lng, $colId, $type, Request $request)
    {
        // return 1;
        // return $request;

        $lng = convert18n($lng);

        // if ($request->date) {

        //     $date = $request->date;
        // } else {
        //     $date = Carbon::now();
        // }
        // return  $request->date;
        // При конфигурация на закрито НС и все още не е активиран архив
        if ($this->endNS() and  $this->collectionCurrentNS(currentNSCID())) {
            $date = '9999-12-31';
        } else {
            if ($request->date) {

                $date = $request->date;
            } else {
                $date = Carbon::now();
            }
        }



        if (!DB::table('A_ns_Coll as cl')
            ->where('cl.A_ns_C_id', $colId)
            // ->where('cl.A_ns_CT_id', $type)
            ->where(function ($query) use ($type) {
                $query->where('cl.A_ns_CT_id', $type)
                    ->orWhere('cl.A_ns_CT_id', 4)
                    ->orWhere('cl.A_ns_CT_id', 9);
            })
            ->exists()) {
            $response = Response::json([
                'status' => false,
                'message' => 'Record not found',
            ], 404);

            return $response;
        }






        $colData = DB::table('A_ns_Coll as cl')
            ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
                $join->on('c18n.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('c18n.C_Lang_id', '=',  $lng);
            })
            ->leftjoin('A_ns_ColData as cd', function ($join) use ($lng) {
                $join->on('cd.A_ns_C_id', '=', 'cl.A_ns_C_id')
                    ->where('cd.C_Lang_id', '=',  $lng);
            })
            // 
            ->where('cl.A_ns_C_id', $colId)
            ->select(
                'cl.A_ns_C_id',
                'c18n.A_ns_CL_value',
                'cl.A_ns_C_active_count',
                'cd.A_ns_CDemail',
                'cd.A_ns_CDroom',
                'cd.A_ns_CDphone',
                'cd.A_ns_CDrules',

            )
            ->first();
        $colData->A_ns_CDrules = nl2br($colData->A_ns_CDrules);

        // 

        // return 1;
        // return $date;
        $colData->colListMP = DB::table('A_ns_MShip as s')
            ->join('A_ns_Mps as mp', 'mp.A_ns_MP_id', '=', 's.A_ns_MP_id')
            // ->join('A_ns_Coll as cl', 'cl.A_ns_C_id', '=', 's.A_ns_C_id')
            ->join('A_ns_MP_Pos as p', 'p.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
            ->join('A_ns_Mps_Lng as mpl_9', function ($join) use ($lng) {
                $join->on('mpl_9.A_ns_MP_id', '=', 's.A_ns_MP_id')
                    ->where('mpl_9.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MP_Pos_Lng as pl', function ($join) use ($lng) {
                $join->on('pl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                    ->where('pl.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_Va as va', 'va.A_ns_Va_id', '=', 'mp.A_ns_Va_id')
            ->where('mp.C_St_id', 1)
            ->where('s.A_ns_C_id', $colId);
        if ($date != '9999-12-31') {
            $colData->colListMP->where('s.A_ns_MSP_date_T', '>=', $date)
                ->where('s.A_ns_MSP_date_F', '<=', $date);
        }

        $colData->colListMP->select(
            // 'cl.A_ns_C_id',
            // 'cl.A_ns_C_active_cnt',
            // 'c18n.A_ns_CL_value',
            's.A_ns_MSP_date_F',
            's.A_ns_MSP_date_T',
            'mp.A_ns_MP_FM',
            'mp.A_ns_MP_id',
            // 'mp.A_ns_MP_BDate',
            // 'mp.A_ns_B_Country',
            // 'mp.A_ns_MP_fbook',
            // 'mp.A_ns_MP_Email',
            // 'mp.A_ns_MP_url',
            'mp.A_ns_MP_leg_count',
            'mp.A_ns_MP_com_count',
            'mp.A_ns_MP_del_count',
            'mp.A_ns_MP_frd_count',
            'mpl_9.A_ns_MPL_Name1',
            'mpl_9.A_ns_MPL_Name2',
            'mpl_9.A_ns_MPL_Name3',
            'pl.A_ns_MP_PosL_value',
            'va.A_ns_Va_name'

        )
            ->groupBy('s.A_ns_MP_id')
            ->orderBy('p.A_ns_MP_Pos_prior', 'desc')
            ->orderBy('mpl_9.A_ns_MPL_Name1', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name2', 'asc')
            ->orderBy('mpl_9.A_ns_MPL_Name3', 'asc');
        $colData->colListMP = $colData->colListMP->get();



        $response = Response::json($colData, 200);
        return $response;
    }

    public function MpProfile($lng, $id)
    {

        $lng = convert18n($lng);

        $MpProfile = DB::table('A_ns_Mps as mp')
            ->join('A_ns_Mps_Lng as mpl_9', function ($join) use ($lng) {
                $join->on('mpl_9.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                    ->where('mpl_9.C_Lang_id', '=',  $lng);
            })
            // ->join('A_ns_Coll_Lng as c18n', function ($join) use ($lng) {
            //     $join->on('c18n.C_Nav_id', '=', 'cl.C_Nav_id')
            //         ->where('n18n.C_Lang_id', '=',  $lng);
            // })
            ->leftjoin('A_ns_Coal as cl', 'cl.A_ns_Coal_id', '=', 'mp.A_ns_Coal_id')
            ->leftjoin('A_ns_Coal_Lng as cll', function ($join) use ($lng) {
                $join->on('cll.A_ns_Coal_id', '=', 'cl.A_ns_Coal_id')
                    ->where('cll.C_Lang_id', '=',  $lng);
            })
            ->join('A_ns_MR_Lng as mrl', 'mrl.A_ns_MR_id', '=', 'mp.A_ns_MR_id')
            ->join('A_ns_Va as va', 'va.A_ns_Va_id', '=', 'mp.A_ns_Va_id')
            ->where('mp.A_ns_MP_id', $id)
            ->where('mp.C_St_id', 1)
            ->select(
                'mp.A_ns_MP_FM',
                'mp.A_ns_MP_BDate',
                'mp.A_ns_B_Country',
                'mp.A_ns_B_Sity as A_ns_B_City',
                'mp.A_ns_MP_fbook',
                'mp.A_ns_MP_Email',
                'mp.A_ns_MP_url',
                'mp.A_ns_MP_leg_count',
                'mp.A_ns_MP_com_count',
                'mp.A_ns_MP_del_count',
                'mp.A_ns_MP_frd_count',
                'mpl_9.*',
                'cll.A_ns_CoalL_value',
                'cl.A_ns_Coal_Prs',
                'mrl.A_ns_MRL_value',
                'va.A_ns_Va_name',
                'va.A_ns_Va_id',
                'va.A_ns_Va_name'

            )
            ->first();


        if ($MpProfile) {
            if ($lng == 2) {
                if ($MpProfile->A_ns_MPL_City) {

                    $MpProfile->A_ns_B_City = $MpProfile->A_ns_MPL_City;
                } else {
                    $MpProfile->A_ns_B_City = C2L($MpProfile->A_ns_B_City);
                }

                if ($MpProfile->A_ns_B_Country) {

                    $ctl = DB::table('C_Country as ct')
                        ->where('ct.C_Country_name', 'like', $MpProfile->A_ns_B_Country)
                        ->join('C_Country_Lng as ctl', function ($join) use ($lng) {
                            $join->on('ctl.C_Country_id', '=', 'ct.C_Country_id')
                                ->where('ctl.C_Lang_id', '=',  $lng);
                        });

                    $ctl->select(
                        'ctl.C_CountryL_value',

                    );
                    $ctlExists =  $ctl->exists();
                    if ($ctlExists) {

                        $MpProfile->A_ns_B_Country = $ctl->first()->C_CountryL_value;
                    } else {
                        $MpProfile->A_ns_B_Country = null;
                    }
                }
            }


            // общини
            $MpProfile->munList = DB::table('A_ns_Va_Mun as mn')
                ->where('mn.A_ns_Va_id', $MpProfile->A_ns_Va_id)
                ->select(
                    'mn.A_ns_Va_M_id',
                    'mn.A_ns_Va_M_name',
                )
                ->get();

            $MpProfile->expenseList = DB::table('A_ns_MP_Expenses')
                ->where('A_ns_MP_id', $MpProfile->A_ns_MP_id)
                ->whereNull('deleted_at')
                ->select(
                    'A_ns_MP_Ex_date',
                    'A_ns_MP_Ex_purpose',
                    'A_ns_MP_Ex_rcpt',
                    'A_ns_MP_Ex_amount',
                )
                ->get();

            //списък професии
            $MpProfile->prsList = DB::table('A_ns_Mps as mp')
                ->join('A_ns_MP_Prs as pr', 'pr.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                ->join('A_ns_MP_Pr_T_Lng as prl', function ($join) use ($lng) {
                    $join->on('prl.A_ns_MP_Pr_T_id', '=', 'pr.A_ns_MP_Pr_T_id')
                        ->where('prl.C_Lang_id', '=',  $lng);
                })

                ->where('mp.A_ns_MP_id', $id)
                ->select(
                    'prl.*',
                    'pr.A_ns_MP_Pr_order'

                )
                ->orderBy('A_ns_MP_Pr_order', 'asc')
                ->get();

            // списък езици


            $MpProfile->lngList = DB::table('A_ns_Mps as mp')
                ->join('A_ns_MP_Lngs as lng', 'lng.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                ->join('A_ns_MP_Lngs_T_Lng as lgl', function ($join) use ($lng) {
                    $join->on('lgl.A_ns_MP_Lng_Т_id', '=', 'lng.A_ns_MP_Lng_Т_id')
                        ->where('lgl.C_Lang_id', '=',  $lng);
                })

                ->where('mp.A_ns_MP_id', $id)
                ->select(
                    'lgl.*'

                )
                ->get();



            // списък научни степени
            $MpProfile->ScList = DB::table('A_ns_Mps as mp')
                ->join('A_ns_MP_Sc as sc', 'sc.A_ns_MP_id', '=', 'mp.A_ns_MP_id')
                ->join('A_ns_MP_Sc_T_Lng as scl', function ($join) use ($lng) {
                    $join->on('scl.A_ns_MP_Sc_T_id', '=', 'sc.A_ns_MP_Sc_T_id')
                        ->where('scl.C_Lang_id', '=',  $lng);
                })

                ->where('mp.A_ns_MP_id', $id)
                ->select(
                    'scl.*'

                )
                ->get();


            // Участия в предишни НС
            $MpProfile->oldnsList = DB::table('A_ns_Old as d')
                ->join('A_ns_Assembly as a', 'a.A_ns_id', '=', 'd.A_ns_id')
                ->join('A_ns_Assembly_Lng as al', function ($join) use ($lng) {
                    $join->on('al.A_ns_id', '=', 'd.A_ns_id')
                        ->where('al.C_Lang_id', '=',  $lng);
                })

                ->where('d.A_ns_MP_id', $id)
                ->select(
                    'al.*',
                    'a.A_ns_folder',

                )
                ->get();

            // парламентарна дейност
            $MpProfile->mshipList = DB::table('A_ns_MShip as s')
                ->join('A_ns_MP_Pos_Lng as psl', function ($join) use ($lng) {
                    $join->on('psl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                        ->where('psl.C_Lang_id', '=',  $lng);
                })
                ->join('A_ns_Coll as c', 'c.A_ns_C_id', '=', 's.A_ns_C_id')
                ->join('A_ns_Coll_Lng as cl', function ($join) use ($lng) {
                    $join->on('cl.A_ns_C_id', '=', 's.A_ns_C_id')
                        ->where('cl.C_Lang_id', '=',  $lng);
                })

                ->where('s.A_ns_MP_id', $id)
                ->where('c.C_St_id', 1)
                // ->whereNull('s.deleted_at')
                ->select(
                    'cl.A_ns_CL_value',
                    'psl.A_ns_MP_PosL_value',
                    'c.A_ns_CT_id',
                    's.*'

                )
                ->orderBy('c.A_ns_CT_id', 'asc')
                ->get();


            // внесени актове
            $MpProfile->importActList = DB::table('L_Acts_Importer as li')
                ->join('L_Acts as l', 'l.L_Act_id', '=', 'li.L_Act_id')
                ->join('L_Acts_Lng as ll', function ($join) use ($lng) {
                    $join->on('ll.L_Act_id', '=', 'l.L_Act_id')
                        ->where('ll.C_Lang_id', '=',  $lng);
                })


                ->where('li.A_ns_MP_id', $id)
                ->whereNull('l.deleted_at')
                ->select(
                    'l.*',
                    'll.L_ActL_title'

                )
                ->orderBy('ll.L_ActL_title', 'asc')
                ->get();

            // парламентарен контрол
            $MpProfile->controlList = DB::table('PK_VNOSITELI as v')
                ->join('PK_VAPROS as q', 'q.ID', '=', 'v.ID_VAPROS')

                ->join('PK_VAPROS_Type_Lng as tl1', function ($join) use ($lng) {
                    $join->on('tl1.PK_VA_T_id', '=', 'q.Type')
                        ->where('tl1.C_Lang_id', '=',  $lng);
                })

                ->join('PK_VAPROS_Type_Lng as tl2', function ($join) use ($lng) {
                    $join->on('tl2.PK_VA_T_id', '=', 'q.VID_OTG')
                        ->where('tl2.C_Lang_id', '=',  $lng);
                })

                ->join('A_ns_Mps_Lng as mpl_9', function ($join) use ($lng) {
                    $join->on('mpl_9.A_ns_MP_id', '=', 'q.A_ns_MP_id')
                        ->where('mpl_9.C_Lang_id', '=',  $lng);
                })
                ->join('A_ns_MShip as s', 's.A_ns_MP_id', '=', 'mpl_9.A_ns_MP_id')
                ->join('A_ns_MP_Pos_Lng as psl', function ($join) use ($lng) {
                    $join->on('psl.A_ns_MP_Pos_id', '=', 's.A_ns_MP_Pos_id')
                        ->where('psl.C_Lang_id', '=',  $lng);
                })


                ->where('v.A_ns_MP_id', $id)
                ->where('q.STATUS', 837)
                // ->whereNull('l.deleted_at')
                ->select(
                    'q.ANOT',
                    'q.DAT_OTG',
                    'q.DAT_PISOTG',
                    'q.VID_OTG',
                    'q.DAT_VRACHVANE',
                    'mpl_9.A_ns_MPL_Name1',
                    'psl.A_ns_MP_PosL_value',
                    'tl1.PK_VA_TL_value as TypeQuestion',
                    'tl2.PK_VA_TL_value as TypeAnswer',

                )
                // ->orderBy('ll.L_ActL_title', 'asc')
                ->get();



            //предложения към законопроекти за второ четене
            $MpProfile->legImportList = DB::table('L_Act_Pr_Importer as i')
                ->join('L_Acts_Proposal as p', 'p.ID', '=', 'i.ID_DOC')
                ->join('L_Acts as a', 'a.L_Act_id', '=', 'p.L_Act_id')



                ->where('i.A_ns_MP_id', $id)
                ->where('a.L_Act_T_id', 1)
                ->whereNull('a.deleted_at')
                ->select(
                    'p.*',

                )
                ->get();


            //Решения и становища по чл.154 от ПОДНС
            $MpProfile->penaltyColList = DB::table('A_ns_Coll_Pen as p')
                ->join('A_ns_Coll_Pen_Types as t', 't.A_ns_C_PenT_id', '=', 'p.A_ns_C_PenT_id')
                ->where('p.A_ns_MP_id', $id)
                ->select(
                    'p.A_ns_C_Pen_date',
                    'p.A_ns_C_Pen_id',
                    'p.A_ns_C_Pen_text',
                    't.A_ns_C_PenT_name',
                    'p.A_ns_C_id',

                )
                ->get();

            //Наложени дисциплинарни мерки по чл.156 от ПОДНС
            // $PenaltyNS = DB::table('A_ns_MP_Pen as p')
            //     ->where('p.A_ns_MP_id', $id)
            //     ->select(
            //         'p.*',
            //     )
            //     ->get();

            $MpProfile->penaltyNsList = DB::table('A_ns_MP_Pen as p')
                ->join('A_ns_MP_Pen_Activity as a', 'a.A_ns_MP_Pen_id', '=', 'p.A_ns_MP_Pen_id')
                ->join('A_ns_MP_Pen_Type as t', 't.A_ns_MP_PenT_id', '=', 'a.A_ns_MP_PenT_id')
                ->join('A_ns_MP_Pen_Status as s', 's.A_ns_MP_PenS_id', '=', 'a.A_ns_MP_PenS_id')
                ->where('p.A_ns_MP_id', $id)
                ->select(
                    's.A_ns_MP_PenS_name',
                    'p.*',
                    'a.*',
                    't.*',

                )
                ->get();
            $MpProfile->strList = DB::table('A_ns_Satr')
                ->where('ns_Satr_SID', $id)
                ->where('ns_Satr_date_to', '>=',  Carbon::now())
                ->select(
                    'ns_Satr_id',
                    'ns_Satr_name',
                    'ns_Satr_edu',
                    'ns_Satr_area',

                )
                ->get();
        }


        $response = Response::json($MpProfile, 200);
        return $response;
    }


    public function VaList()
    {

        $VaList = DB::table('A_ns_Va')
            ->select('A_ns_Va_id as id', 'A_ns_Va_name as label')
            ->get();

        $response = Response::json($VaList, 200);
        return $response;
    }


    public function munList($vaId = 0)
    {
        $munList = DB::table('A_ns_Va_Mun');

        if ($vaId) {
            $munList->where('A_ns_Va_id', $vaId);
        }

        $munList = $munList->get();

        $response = Response::json($munList, 200);
        return $response;
    }

    public function coalList($lng)
    {

        $lng = convert18n($lng);
        $coalList = DB::table('A_ns_Coal as t')
            ->join('A_ns_Coal_Lng as n18', function ($join) use ($lng) {
                $join->on('n18.A_ns_Coal_id', '=', 't.A_ns_Coal_id')
                    ->where('n18.C_Lang_id', '=',  $lng);
            })
            ->select('t.A_ns_Coal_id', 't.A_ns_Coal_Prs', 'n18.A_ns_CoalL_value')
            ->where('t.A_ns_id', currentNS());

        $coalList = $coalList->get();

        $response = Response::json($coalList, 200);
        return $response;
    }


    public function endNS()
    {

        return DB::table('C_Configuration')
            ->where('CC_key', 'ns-end')
            ->where('CC_value', 1)->exists();
    }

    public function collectionCurrentNS($colId)
    {

        return DB::table('A_ns_Coll')
            ->where('A_ns_id', currentNS())
            ->where('A_ns_C_id', $colId)->exists();
    }
}
