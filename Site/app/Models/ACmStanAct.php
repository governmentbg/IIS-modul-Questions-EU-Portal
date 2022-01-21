<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_StanA_id
 * @property int        $A_Cm_Stanid
 * @property int        $L_Act_id
 * @property int        $A_Cm_StanA_date
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ACmStanAct extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Cm_Stan_Act';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_StanA_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_StanA_id', 'A_Cm_Stanid', 'L_Act_id', 'A_Cm_StanA_date', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_StanA_id' => 'int', 'A_Cm_Stanid' => 'int', 'L_Act_id' => 'int', 'A_Cm_StanA_date' => 'timestamp', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_Cm_StanA_date', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_stan()
    {
        return $this->belongsTo(ACmStan::class, 'A_Cm_Stanid');
    }
}
