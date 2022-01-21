<?php

namespace App\Http\Controllers;

use App\Models\DPetition;
use Illuminate\Support\Facades\Response;
use Illuminate\Http\Request;
use \Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator as FacadesValidator;

class DPetitionController extends Controller
{
    public function store(Request $request)
    {
        // return 1;

        $validator = FacadesValidator::make($request->all(), [
            'D_Pt_author' => 'required',
            'D_Pt_email' => 'required|email',
            'D_Pt_body' => 'required',
            // 'D_Pt_file' => 'mimes:jpg,png,pdf,docx,xlsx,csv'
        ]);



        if ($validator->fails()) {


            $response = Response::json([
                'submit' => 1,
                'status' => false,
                'message' => 'Непълни данни',

            ], 200);

            return $response;
        }

        if ($request->D_Pt_file) {

            $D_Pt_file = $request->file('D_Pt_file')->getClientOriginalName();
            $prefix = date("YmdGis") . rand(999, 9999) . "_";
            $D_Pt_file =  $prefix . $D_Pt_file;
            // $destination = 'storage/pub/petition/';
            $destination = 'pub/petition/';
            $path = 'pub/petition/';
            $request->file('D_Pt_file')->move($destination, $D_Pt_file);

            $D_Pt_file_name = $path . $D_Pt_file;
        } else {
            $D_Pt_file_name = null;
        }


        $discuss = new DPetition(array(
            'D_Pt_author' => trim($request->D_Pt_author),
            'D_Pt_email' => trim($request->D_Pt_email),
            'D_Pt_topic' => trim($request->D_Pt_topic),
            'D_Pt_body' => trim($request->D_Pt_body),
            'D_Pt_date' => trim($request->D_Pt_date),
            'D_Pt_file' => $D_Pt_file_name,


        ));
        $discuss->save();


        $response = Response::json([
            'submit' => 2,
            'status' => true,
            'message' => 'Успешно подадени данни',

        ], 200);


        return $response;
    }

    public function showPetition($id)
    {

        $profile = DB::table('D_Petition as l')
            ->where('C_St_id', 1)
            ->whereNull('deleted_at')
            ->where('D_Pt_id', $id)
            ->orderBy('created_at', 'desc')
            ->select(
                'D_Pt_id',
                'D_Pt_author',
                'D_Pt_email',
                'D_Pt_date',
                'D_Pt_topic',
                'D_Pt_body',
                'created_at'
            )
            ->first();

        $response = Response::json($profile, 200);
        return $response;
    }

    public function listPetitions()
    {

        $list = DB::table('D_Petition as l')
            ->where('C_St_id', 1)
            ->whereNull('deleted_at')
            ->orderBy('created_at', 'desc')
            ->select(
                'D_Pt_id',
                'D_Pt_author',
                'D_Pt_email',
                'D_Pt_topic',
                'D_Pt_body',
                'D_Pt_date',
                'D_Pt_file',
                'created_at',
            )
            ->get();

        $response = Response::json($list, 200);
        return $response;
    }
}
