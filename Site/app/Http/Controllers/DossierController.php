<?php


namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class DossierController extends Controller
{
    public function fnDossier(Request $request)
    {


        if (isset($request->ActT_id)) {
            $ActT_id =  (int) $request->ActT_id["id"];
        } else {
            $ActT_id =  null;
        }

        if (isset($request->ActTh_id)) {
            $ActTh_id =  (int) $request->ActTh_id["id"];
        } else {
            $ActTh_id =  null;
        }

        $fnStringIme = cleanUp($request->fnStringIme);
        $RN_FULL = cleanUp($request->RN_FULL);
        $RN_FULL = cleanUp($request->RN_FULL);
        $dName = cleanUp($request->dName);



        $funnel = DB::table('EURO_DOSSIER as d')


            ->join('EURO_ACT_NEW as act', 'act.ID', '=', 'd.EURO_ACT_NEW_ID')
            ->join('EURO_ACT_Type as t', 't.ActT_id', '=', 'act.VID_ACT');


        $funnel->select(
            'd.ID',
            'd.NАМЕ as dName',
            'act.RN_FULL',
            't.ActT_name',
        );



        if ($fnStringIme) {
            $funnel->where('act.IME', 'like', '%' . $fnStringIme . '%');
        }
        if ($dName) {
            $funnel->where('d.NАМЕ', 'like', '%' . $dName . '%');
        }
        if ($RN_FULL) {
            $funnel->where('act.RN_FULL', 'like', '%' . $RN_FULL . '%');
        }
        if ($ActT_id) {
            $funnel->where('act.VID_ACT', '=', $ActT_id);
        }

        if ($ActTh_id) {
            $funnel->where('d.EU_th_id', '=', $ActTh_id);
        }




        $funnel->where('d.ON_SITE_YESNO', '!=', 2);
        //     ->where('q.Type', $type);


        $funnel->orderBy('d.ID', 'desc');
        if ($request->search) {
            $funnel->take(1200);
        } else {

            $funnel->take(1000);
        }
        $funnel = $funnel->get();



        $response = Response::json($funnel, 200);
        return $response;
    }


    public function dsProfile($id)
    {


        $profile = DB::table('EURO_DOSSIER as d')


            ->join('EURO_ACT_NEW as act', 'act.ID', '=', 'd.EURO_ACT_NEW_ID')
            ->join('EURO_ACT_Type as t', 't.ActT_id', '=', 'act.VID_ACT')
            ->leftjoin('EURO_Theme as th', 'th.EU_th_id', '=', 'd.EU_th_id');


        $profile->select(
            'd.ID',
            'd.EURO_ACT_NEW_ID',
            'd.NАМЕ as dName',
            't.AcT_parent_id',
            't.ActT_name',
            'act.URL as aURL',
            'act.IME as aName',
            'act.CELEX',
            'act.RN_FULL',
            'act.NOTE_AB2',
            'act.NOTE_AB3',
            'act.NOTE_AB4I',
            'act.NOTE_AB4D',
            'd.ZAKON_NAME',
            'd.ZAKON_NAME_V',
            'd.ZAKON_DV_BR_V',
            'd.ZAKON_DV_GOD_V',
            'd.NOTE_V',
            'd.REQ_MEASURE_YESNO',
            'd.SECTION_V_YESNO',
            'd.SECTION_V_TEXT',
            'd.NOTIFICATIONS_YESNO',
            'd.NOTIFICATIONS_NUMBER',
            'd.NOTIFICATIONS_DATE',
            'd.NOTIFICATIONS_URL',
            'd.SECTION_I1',
            'd.SECTION_I2',
            'd.SECTION_I4',
            'd.DATE_REG',

            'act.NOTE_SR',
            'act.NOTE_AB2',
            'act.NOTE_AB3',
            'act.NOTE_AB4I',
            'act.NOTE_AB4D',
            'act.IZPALNENIE_ACTS_YESNO',
            'act.IZPALNENIE_ACTS_YESNO',
            'act.SECTION_G_YESNO',
            'act.SECTION_D_YESNO',
            'act.SECTION_D_TEXT',
            'act.SECTION_D_URL',
            'act.SECTION_E_YESNO',
            'act.SECTION_E_YESNO',
            'act.SECTION_Z_TEXT',





            'th.EU_th_id',
            'th.EU_th_name',
        );



        $profile->where('d.ON_SITE_YESNO', '!=', 2);
        $profile->where('d.ID', $id);
        $profile->orderBy('d.ID', 'desc');
        $profile = $profile->first();

        $profile->z_h = DB::table('EURO_SECTION_Z as z')
            ->where('z.EURO_ACT_NEW_ID', $profile->EURO_ACT_NEW_ID)
            ->select(
                'z.ID',
                'z.NAME_BG_ACT',
                'z.DV_BROI',
                'z.DV_GODINA',
                'z.DATE_REG',
            )
            ->get();

        $profile->e_f = DB::table('EURO_SECTION_E as e')
            ->where('e.EURO_ACT_NEW_ID', $profile->EURO_ACT_NEW_ID)
            ->select(
                'e.ID',
                'e.NAME',
                'e.URL',
                'e.NOTE',
                'e.DATE_REG',
            )
            ->get();
        $role = ["30245", "30246", "30247", "30248", "30249", "30250", "30293"];
        $profile->a2 = $this->ActRel($profile->EURO_ACT_NEW_ID, $role, 0, 0);

        $role = ["30251", "30252", "30253", "30287", "30288", "30289", "30292"];
        $profile->a3 = $this->ActRel(0, 0, $profile->EURO_ACT_NEW_ID, $role);


        $role = ["30290"];
        $profile->a41 = $this->ActRel($profile->EURO_ACT_NEW_ID, $role, 0, 0);


        $role = ["30307"];
        $profile->a42 = $this->ActRel($profile->EURO_ACT_NEW_ID, $role, 0, 0);


        $role = ["30309"];
        $profile->g_d = $this->ActRel($profile->EURO_ACT_NEW_ID, $role, 0, 0);
        // $profile->g_d = $this->ActRel(0, 0, $profile->EURO_ACT_NEW_ID, $role);

        $type1 = ["30225", "30266", "30268", "30228", "30267"];
        $type2 = [];
        $profile->a5 = $this->dossRel($profile->ZAKON_NAME_V, $profile->ZAKON_DV_BR_V, $profile->ZAKON_DV_GOD_V, $profile->ID, $profile->AcT_parent_id, 0, 5, $profile->NOTE_V);

        $type2 = ["30225", "30266", "30268", "30228", "30267"];
        $type1 = [];
        $profile->b = $this->dossRel($profile->ZAKON_NAME_V, $profile->ZAKON_DV_BR_V, $profile->ZAKON_DV_GOD_V, $profile->ID, 0, $profile->AcT_parent_id);




        $response = Response::json($profile, 200);
        return $response;
    }


    public function ActRel($act1, $role1, $act2, $role2)
    {
        // $rule1 = ["30245", "30246", "30247"];

        $rel = DB::table('EURO_ACTS_CONN as eac')
            ->join('EURO_ACT_NEW as ea1', 'ea1.ID', '=', 'eac.EURO_ACT_NEW_ID1')
            ->join('EURO_ACT_NEW as ea2', 'ea2.ID', '=', 'eac.EURO_ACT_NEW_ID2');
        if ($act1) {
            $rel->where('eac.EURO_ACT_NEW_ID1', $act1);
        }

        if ($act2) {
            $rel->where('eac.EURO_ACT_NEW_ID2', $act2);
        }

        if ($role1) {
            $rel->leftjoin('EU_Act_rel as rt', 'rt.EU_rel_id', '=', 'eac.ROLE_ACT1')
                ->whereIn('eac.ROLE_ACT1', $role1);
        }
        if ($role2) {
            $rel->leftjoin('EU_Act_rel as rt', 'rt.EU_rel_id', '=', 'eac.ROLE_ACT2')
                ->whereIn('eac.ROLE_ACT2', $role2);
        }

        // ->where('eac.EURO_ACT_NEW_ID1', $profile->EURO_ACT_NEW_ID)
        // ->orWhere('eac.EURO_ACT_NEW_ID1', $profile->EURO_ACT_NEW_ID)

        $rel->select(
            'eac.ID',
            'rt.EU_rel_id',
            'rt.EU_rel_name',
            'eac.EURO_ACT_NEW_ID1',
            'eac.ROLE_ACT1',
            'eac.EURO_ACT_NEW_ID2',
            'eac.ROLE_ACT2',
            'eac.NOTE',
            'eac.USER_REG',
            'eac.DATE_REG',
            'ea1.IME as IME1',
            'ea1.URL as URL1',
            'ea1.NOTE_SR as NOTE_SR',
            'ea1.RN_FULL as RNFULL1',
            'ea1.GODINA as GOD1',
            'ea2.IME as IME2',
            'ea2.URL as URL2',
            'ea2.NOTE_SR as NOTE_SR2',
            'ea2.RN_FULL as RNFULL2',
            'ea2.GODINA as  GOD2',
        )

            ->orderBy('eac.ROLE_ACT1', 'asc')
            ->orderBy('eac.ROLE_ACT2', 'asc')
            ->orderBy('ea1.GODINA', 'asc')
            ->orderBy('ea2.GODINA', 'asc')
            ->orderBy('ea1.RN', 'asc')
            ->orderBy('ea2.RN', 'asc');

        $rel = $rel->get();
        return $rel;
    }


    public function dossRel($zname, $brdv, $goddv, $did, $parent1, $parent2, $a5 = 0, $note = 0)
    {


        $rel = DB::table('EURO_DOSSIER as d')
            ->join('EURO_ACT_NEW as act', 'act.ID', '=', 'd.EURO_ACT_NEW_ID')
            ->join('EURO_ACT_Type as t', 't.ActT_id', '=', 'act.VID_ACT');


        // ->where('eac.EURO_ACT_NEW_ID1', $profile->EURO_ACT_NEW_ID)
        // ->orWhere('eac.EURO_ACT_NEW_ID1', $profile->EURO_ACT_NEW_ID)
        $rel->where('d.ID', '!=', $did)
            ->where('d.ZAKON_NAME_V', $zname)
            ->where('d.ZAKON_DV_BR_V', $brdv)
            ->where('d.ZAKON_DV_GOD_V', $goddv);
        if ($a5 == 5) {
            $rel->where('t.AcT_parent_id', $parent1);
        }
        $rel->where('t.AcT_parent_id', '!=', $parent2)
            // ->whereIn('act.VID_ACT', $type1)
            // ->whereNotIn('act.VID_ACT', $type2)
        ;

        if ($a5 == 5) {
            if (is_null($note)) {
                $rel->whereNull('d.NOTE_V');
            } else {
                $rel->where('d.NOTE_V', $note);
            }
        }
        $rel->select(
            'd.ID',
            'd.NАМЕ as dName',
        );



        $rel = $rel->get();
        return $rel;
    }


    public function dossType()
    {


        $type = DB::table('EURO_DOSSIER as d')
            ->join('EURO_ACT_NEW as act', 'act.ID', '=', 'd.EURO_ACT_NEW_ID')
            ->join('EURO_ACT_Type as t', 't.ActT_id', '=', 'act.VID_ACT')

            ->select(
                't.ActT_id',
                't.ActT_name',
            )
            ->groupBy('t.ActT_id')
            ->get();
        $response = Response::json($type, 200);
        return $response;
    }


    public function dossTheme()
    {


        $theme = DB::table('EURO_Theme')
            ->orderBy('EU_th_name', 'asc')
            ->get();
        $response = Response::json($theme, 200);
        return $response;
    }
}
