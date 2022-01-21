<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_Sitid
 * @property int        $Cts_id
 * @property string     $A_Cm_Sit_body_1
 * @property string     $A_Cm_Sit_body_2
 * @property string     $A_Cm_Sit_room
 * @property int        $A_Cm_SitPid
 * @property DateTime   $A_Cm_Sit_date
 * @property int        $A_ns_C_id
 * @property int        $Pl_ProgT_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class TSACmSit extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'TS_A_Cm_Sit';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_Sitid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_Sitid', 'Cts_id', 'A_Cm_Sit_body_1', 'A_Cm_Sit_body_2', 'A_Cm_Sit_room', 'A_Cm_SitPid', 'A_Cm_Sit_date', 'A_Cm_Sit_time', 'A_ns_C_id', 'Pl_ProgT_id', 'A_Cm_Sit_Cal', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_Sitid' => 'int', 'Cts_id' => 'int', 'A_Cm_Sit_body_1' => 'string', 'A_Cm_Sit_body_2' => 'string', 'A_Cm_Sit_room' => 'string', 'A_Cm_SitPid' => 'int', 'A_Cm_Sit_date' => 'datetime', 'A_ns_C_id' => 'int', 'Pl_ProgT_id' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_Cm_Sit_date', 'created_at', 'updated_at', 'deleted_at'
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
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_coll()
    {
        return $this->belongsTo(ANsColl::class, 'A_ns_C_id');
    }

    public function eq_ts()
    {
        return $this->belongsTo(CThemeSite::class, 'Cts_id');
    }

    public function eq_sitp()
    {
        return $this->belongsTo(ACmSitP::class, 'A_Cm_SitPid');
    }
}
