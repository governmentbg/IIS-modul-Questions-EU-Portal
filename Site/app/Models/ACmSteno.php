<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_Stid
 * @property int        $A_ns_C_id
 * @property Date       $A_Cm_St_date
 * @property int        $A_Cm_Sitid
 * @property string     $A_Cm_St_sub
 * @property string     $A_Cm_St_text
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ACmSteno extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Cm_Steno';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_Stid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_Stid', 'A_ns_C_id', 'A_Cm_St_date', 'A_Cm_St_time', 'A_Cm_Sitid', 'A_Cm_St_sub', 'A_Cm_St_text', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_Stid' => 'int', 'A_ns_C_id' => 'int', 'A_Cm_St_date' => 'date', 'A_Cm_Sitid' => 'int', 'A_Cm_St_sub' => 'string', 'A_Cm_St_text' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_Cm_St_date', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_coll()
    {
        return $this->belongsTo(ANsColl::class, 'A_ns_C_id');
    }
    public function eq_sit()
    {
        return $this->belongsTo(ACmSit::class, 'A_Cm_Sitid');
    }
}
