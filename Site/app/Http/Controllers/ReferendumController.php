<?php

namespace App\Http\Controllers;


use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class ReferendumController extends Controller
{


    public function refList()
    {
        // return 1;


        $refList = DB::table('Rf_application as r')
            ->leftjoin('Rf_status as s', 's.Rf_st_id', '=', 'r.Rf_st_id')

            ->select(
                'r.Rf_ap_id',
                'r.Rf_ap_question',
                'r.Rf_ap_info',
                'r.Rf_ap_ann_date',
                's.Rf_st_name',

            )
            ->whereNull('r.deleted_at')
            ->orderBy('r.Rf_ap_ann_date', 'desc')
            ->orderBy('r.Rf_ap_id', 'desc')
            ->get();




        $response = Response::json($refList, 200);
        return $response;
    }

    public function refProfile($id)
    {
        // 

        $refList = DB::table('Rf_application as r')
            ->join('Rf_status as s', 's.Rf_st_id', '=', 'r.Rf_st_id')
            ->leftjoin('Rf_importer_type as it', 'it.Rf_impT_id', '=', 'r.Rf_impT_id')

            ->select(
                'r.Rf_ap_id',
                'r.Rf_ap_question',
                'r.Rf_ap_info',
                'r.Rf_ap_ann_date',
                's.Rf_st_name',
                'r.Rf_ap_term_date',
                'r.Rf_ap_reg_date',
                'it.Rf_impT_name',
                'r.Rf_ap_importer',
                // 'r.Rf_ap_proposal',
                DB::raw("IF(SUBSTR(r.Rf_ap_proposal, 1,4)='pub/',CONCAT('/',r.Rf_ap_proposal),CONCAT('/pub/referendum/',r.Rf_ap_proposal))  as Rf_ap_proposal"),
                DB::raw("IF(SUBSTR(r.Rf_ap_ann_file, 1,4)='pub/',CONCAT('/',r.Rf_ap_ann_file),CONCAT('/pub/referendum/',r.Rf_ap_ann_file))  as Rf_ap_ann_file"),
                'r.Rf_ap_date',
            )
            ->where('r.Rf_ap_id', $id)
            ->whereNull('r.deleted_at')
            ->orderBy('r.Rf_ap_ann_date', 'desc')
            ->orderBy('r.Rf_ap_id', 'desc')
            ->first();

        $refList->resource = DB::table('Rf_resource as r')
            ->join('Rf_resource_type as t', 't.Rf_rsT_id', '=', 'r.Rf_rsT_id')
            ->select(
                'r.Rf_rs_id',
                't.Rf_rsT_id',
                't.Rf_rsT_name',
                'r.Rf_rs_name',
                'r.Rf_rs_date',
                'r.Rf_rs_url',
                // 'r.Rf_rs_file',
                DB::raw("IF(SUBSTR(r.Rf_rs_file, 1,4)='pub/',CONCAT('/',r.Rf_rs_file),CONCAT('/pub/referendum/',r.Rf_rs_file))  as Rf_rs_file"),
            )
            ->where('r.Rf_ap_id', $id)
            ->whereNull('r.deleted_at')
            ->get();




        $response = Response::json($refList, 200);
        return $response;
    }


    public function refInfo()
    {
        // return 1;


        $declInfo = 'Законът за пряко участие на гражданите в държавната власт и местното самоуправление е приет от Народното събрание на 29 май 2009 г. и е обнародван в „Държавен вестник” в бр. 44 от 2009 г, последно изменение и допълнение, бр.56 от 24.07.2015 г., в сила от 24.07.2015 г.<br /><br />
Законът урежда условията, организацията и реда за пряко участие на гражданите на Република България при осъществяване на държавната и местната власт. Прякото участие се осъществява чрез:

<ol>
<li>референдум;</li>
<li>гражданска инициатива;</li>
<li>общо събрание на населението.</li>
</ol>

Референдумът може да се произвежда на национално и местно ниво, гражданската инициатива  - на национално и местно ниво, а общото събрание на населението - на местно ниво.<br /><br />
Регистърът по Закона за пряко участие на гражданите в държавната власт и местното самоуправление е изработен в изпълнение разпоредбата на чл. 10, ал. 3 от закона, съгласно която Председателят на Народното събрание организира създаването и воденето на публичен регистър, в който се вписват уведомленията за започване на подписка за произвеждане на референдум с точно формулиран въпрос, а след приключването й - предложенията за произвеждане на национален референдум и съответните инициативни комитети.<br /><br />
Национален референдум се произвежда на територията на Република България за пряко решаване от гражданите на въпроси с национално значение от компетентността на Народното събрание. В закона изрично са записани въпросите, които не могат да бъдат решавани чрез национален референдум.<br /><br />
Право да гласуват на национален референдум имат гражданите на Република България с избирателни права, които имат постоянен адрес на територията на страната към деня на насрочване на референдума. <br /><br />
Предложение за произвеждане на национален референдум до Народното събрание може да бъде направено от:

<ol>
<li>не по-малко от една пета от народните представители;</li>
<li>Президента на Републиката;</li>
<li>Министерския съвет;</li>
<li>не по-малко от една пета от общинските съвети в страната;</li>
<li>инициативен комитет на граждани с избирателни права, събрал не по-малко от 200 000 подписа на граждани с избирателни права.</li>
</ol>


Народното събрание приема решение по предложението за референдум на едно гласуване в срок до три месеца от внасянето на предложението, ако са спазени изискванията на закона. С решението Народното събрание може да одобри предложението за произвеждане на референдум с въпроса или въпросите, по които трябва да се проведе гласуването, или мотивирано да отхвърли предложението. Когато предложението е направено от инициативен комитет с подписка, съдържаща подписите на не по-малко от 400 000 български граждани с избирателни права и отговаря на изискванията на закона, Народното събрание е длъжно да приеме решение за произвеждане на референдум.<br /><br />
В едномесечен срок от обнародването на решението на Народното събрание за произвеждане на референдум, Президентът на Републиката определя датата на референдума, която не може да е по-рано от два и по-късно от три месеца от датата на обнародване на решението на Народното събрание.<br /><br />
Организационно-техническата подготовка на националния референдум се осъществява от Министерския съвет.<br /><br />
Решението, прието с национален референдум, не подлежи на последващо одобрение от Народното събрание.

        ';




        $response = Response::json($declInfo, 200);
        return $response;
    }
}
