<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_MP_id
 * @property int        $A_ns_id
 * @property string     $A_ns_MP_person
 * @property int        $A_ns_MP_FM
 * @property int        $A_ns_MP_Mand
 * @property string     $A_ns_B_Sity
 * @property int        $A_ns_B_Arid
 * @property string     $A_ns_B_Country
 * @property int        $A_ns_B_Country_id
 * @property string     $A_ns_MP_EGN
 * @property Date       $A_ns_MP_BDate
 * @property int        $A_ns_MP_Age
 * @property int        $A_ns_MR_id
 * @property int        $A_ns_MP_Edu_id
 * @property int        $A_ns_Va_id
 * @property int        $A_ns_Coal_id
 * @property int        $A_ns_Va_Type
 * @property string     $A_ns_MP_Email
 * @property string     $A_ns_MP_url
 * @property string     $A_ns_MP_phones
 * @property string     $A_ns_MP_fbook
 * @property int        $C_St_id
 * @property int        $C_Agr_id
 * @property int        $sync_ID
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ANsMps extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_ns_Mps';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_MP_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_MP_id', 'A_ns_id', 'A_ns_MP_person', 'A_ns_MP_Name', 'A_ns_MP_FM', 'A_ns_MPGender', 'A_ns_MP_Mand', 'A_ns_B_Sity', 'A_ns_B_Arid', 'A_ns_B_Country', 'A_ns_B_Country_id', 'A_ns_MP_EGN', 'A_ns_MP_BDate', 'A_ns_MP_Age', 'A_ns_MR_id', 'A_ns_MP_Edu_id', 'A_ns_Va_id', 'A_ns_Coal_id', 'A_ns_Va_Type', 'A_ns_MP_Email', 'A_ns_MP_url', 'A_ns_MP_phones', 'A_ns_MP_fbook', 'C_St_id', 'C_Agr_id', 'sync_ID', 'created_at', 'updated_at', 'deleted_at'
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
        'A_ns_MP_id' => 'int', 'A_ns_id' => 'int', 'A_ns_MP_person' => 'string', 'A_ns_MP_FM' => 'int', 'A_ns_MP_Mand' => 'int', 'A_ns_B_Sity' => 'string', 'A_ns_B_Arid' => 'int', 'A_ns_B_Country' => 'string', 'A_ns_B_Country_id' => 'int', 'A_ns_MP_EGN' => 'string', 'A_ns_MP_BDate' => 'date', 'A_ns_MP_Age' => 'int', 'A_ns_MR_id' => 'int', 'A_ns_MP_Edu_id' => 'int', 'A_ns_Va_id' => 'int', 'A_ns_Coal_id' => 'int', 'A_ns_Va_Type' => 'int', 'A_ns_MP_Email' => 'string', 'A_ns_MP_url' => 'string', 'A_ns_MP_phones' => 'string', 'A_ns_MP_fbook' => 'string', 'C_St_id' => 'int', 'C_Agr_id' => 'int', 'sync_ID' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_ns_MP_BDate', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_assembly()
    {
        return $this->belongsTo(ANsAssembly::class, 'A_ns_id');
    }

    public function eq_expenses()
    {
        return $this->hasMany(ANsMPExpenses::class, 'A_ns_MP_id');
    }

    public function eq_meetings()
    {
        return $this->hasMany(MPMeetings::class, 'A_ns_MP_id');
    }

    public function eq_mp()
    {
        return $this->hasMany(MPAbsense::class, 'A_ns_MP_id');
    }
    public function eq_mp_log()
    {
        return $this->hasMany(MPAbsenseLog::class, 'A_ns_MP_id');
    }
}
