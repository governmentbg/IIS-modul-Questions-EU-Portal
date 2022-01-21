<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Support\Facades\DB;

/**
 * @property int        $Pl_ProgP_id
 * @property int        $Pl_Prog_id
 * @property int        $Pl_ProgP_active
 * @property int        $Pl_ProgNo
 * @property int        $L_Act_id
 * @property int        $A_ns_C_id
 * @property int        $sync_ID
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PlProgramPoints extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Pl_Program_Points';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Pl_ProgP_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Pl_ProgP_id', 'Pl_Prog_id', 'Pl_ProgP_active', 'Pl_ProgNo', 'L_Act_id', 'A_ns_C_id', 'Pl_Control_id', 'sync_ID', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'Pl_ProgP_id' => 'int', 'Pl_Prog_id' => 'int', 'Pl_ProgP_active' => 'int', 'Pl_ProgNo' => 'int', 'L_Act_id' => 'int', 'A_ns_C_id' => 'int', 'Pl_Control_id' => 'int', 'sync_ID' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            DB::table('Pl_Program_Points')->where('Pl_ProgP_active', 1)
                ->update(['Pl_ProgP_active' => NULL]);
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_pr_points()
    {
        return $this->hasMany(PlProgramPointsLng::class, 'Pl_ProgP_id');
    }

    public function eq_program()
    {
        return $this->belongsTo(PlProgram::class, 'Pl_Prog_id');
    }
}
