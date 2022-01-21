<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $L_Act_id
 * @property int        $synch_ID_new_id
 * @property int        $L_Act_T_id
 * @property string     $L_Act_sign
 * @property int        $L_Ses_id
 * @property int        $L_Act_new_id
 * @property DateTime   $L_Act_date
 * @property DateTime   $L_Act_date2
 * @property string     $L_Act_date_dv
 * @property string     $L_Act_dv_iss
 * @property int        $L_Act_dv_ID
 * @property string     $L_Act_author_text
 * @property Date       $L_act_proposal_date
 * @property Date       $L_act_proposal_date2
 * @property int        $L_UT
 * @property int        $sync_ID
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class LActs extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'L_Acts';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'L_Act_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'L_Act_id', 'synch_ID_new_id', 'L_Act_T_id', 'L_Act_sign', 'L_Ses_id', 'L_Act_new_id', 'L_Act_date', 'L_Act_date2', 'L_Act_date_dv', 'L_Act_dv_year', 'L_Act_dv_iss', 'L_Act_dv_ID', 'L_Act_author_text', 'L_act_proposal_date', 'L_act_proposal_date2', 'L_UT', 'sync_ID', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'L_Act_id' => 'int', 'synch_ID_new_id' => 'int', 'L_Act_T_id' => 'int', 'L_Act_sign' => 'string', 'L_Ses_id' => 'int', 'L_Act_new_id' => 'int', 'L_Act_date' => 'datetime', 'L_Act_date2' => 'datetime', 'L_Act_date_dv' => 'string', 'L_Act_dv_iss' => 'string', 'L_Act_dv_ID' => 'int', 'L_Act_author_text' => 'string', 'L_act_proposal_date' => 'date', 'L_act_proposal_date2' => 'date', 'L_UT' => 'int', 'sync_ID' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'L_Act_date', 'L_Act_date2', 'L_act_proposal_date', 'L_act_proposal_date2', 'created_at', 'updated_at', 'deleted_at'
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

    public function eq_discuss()
    {
        return $this->hasMany(LActDiscuss::class, 'L_Act_id');
    }
}
