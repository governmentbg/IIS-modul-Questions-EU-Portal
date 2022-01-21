<?php

namespace App\Http\Controllers;


use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;
use \Illuminate\Support\Facades\DB;

class ConflictController extends Controller
{

    public function declList($archive)
    {
        // return 1;

        if ($archive == 1) {
            $аrh = "y";
        } else {
            $аrh = "n";
        }
        $declList = DB::table('R_persons as p')
            ->join('R_institution as i', 'i.instit_id', '=', 'p.instit_id')
            ->join('R_position as ps', 'ps.pos_id', '=', 'p.pos_id')



            ->select(
                'p.person_id',
                'p.name1',
                'p.name2',
                'p.name3',
                'i.instit_name',
                'ps.pos_name',
            )
            ->where('p.arh', $аrh)
            ->orderBy('i.instit_order', 'desc')
            ->orderBy('i.instit_id', 'asc')
            ->orderBy('ps.pos_order', 'desc')
            ->orderBy('ps.pos_order', 'desc')
            ->orderBy('p.name1', 'asc')
            ->orderBy('p.name2', 'asc')
            ->orderBy('p.name3', 'asc')
            ->get();


        //
        foreach ($declList as $key_parent1 => $list) {
            $decl_file = DB::table('R_decl_file as f')
                ->join('R_decl_type as t', 't.decl_type_id', '=', 'f.decl_type_id')

                ->where('person_id', $list->person_id)
                ->select(
                    'f.decl_id',
                    'f.regN',
                    // 'f.decl_file', 
                    DB::raw("IF(SUBSTR(f.decl_file, 1,4)='pub/',CONCAT('/',f.decl_file),CONCAT('/pub/REG/',f.decl_file))  as decl_file"),
                    't.decl_type_id',
                    't.decl_type_text'
                )
                ->get();




            $declList[$key_parent1]->files = $decl_file;
        }






        $response = Response::json($declList, 200);
        return $response;
    }


    public function declInfo()
    {
        // return 1;


        $declInfo = 'Съгласно Закона за противодействие на корупцията и за отнемане на незаконно придобитото имущество и в съответствие с Решение на Народното събрание във връзка с чл. 6, ал. 1 от Закона за противодействие на корупцията и за отнемане на незаконно придобитото имущество президентът, вицепрезидентът, съдиите от Конституционния съд, народните представители, министър-председателят, заместник министър-председателите, министрите, омбудсманът и заместник-омбудсманът, членовете на Висшия съдебен съвет, включително председателите на Върховния касационен съд и на Върховния административен съд и главният прокурор, главният инспектор и инспекторите от Инспектората към Висшия съдебен съвет, председателят и членовете на Сметната палата, управителят, подуправителите и членовете на управителния съвет на Българската народна банка, управителят и подуправителят на Националния осигурителен институт, членовете на органи, които изцяло или частично се избират от Народното събрание подават декларации по чл. 35, ал. 1, т. 1 и 3, ал. 2 и 3 от Закона за противодействие на корупцията и за отнемане на незаконно придобитото имущество пред органа, който го избира или назначава, или пред съответната комисия в Народното събрание. Посочените по-горе лица подават:<br />
<ol><li>
декларация за несъвместимост;</li>
<li>декларация за промяна в декларирани обстоятелства в декларацията по т. 1;</li></ol><br />
При заемането на висша публична длъжност, за която с Конституцията или със закон са установени несъвместимости, лицето подава пред органа по избора или назначаването или пред съответната комисия за лице по чл. 72, ал. 2, т. 1 и 3 декларация за несъвместимост в едномесечен срок от заемането на длъжността.<br />
При промяна на заеманата длъжност лице, което остава задължено по този закон, не подава нова декларация за несъвместимост, освен ако за новата длъжност са предвидени различни несъвместимости.
 Когато лицето е декларирало наличие на несъвместимост, то е длъжно в едномесечен срок от подаване на декларацията да предприеме необходимите действия за отстраняване на несъвместимостта и да представи доказателства за това пред органа по избора или назначаването.<br />
В случай че лицето не предприеме действия за отстраняване на несъвместимостта в срока по ал. 3, органът по избора или назначаването предприема действия за прекратяване на правоотношението.<br />
Когато в специален закон е предвидено задължение за подаване на декларация за несъвместимост от съответните лица преди възникване на трудовото или служебното правоотношение, същите лица не подават допълнителна декларация за несъвместимост след възникване на правоотношението.

<br /><br />
        ';




        $response = Response::json($declInfo, 200);
        return $response;
    }
}
