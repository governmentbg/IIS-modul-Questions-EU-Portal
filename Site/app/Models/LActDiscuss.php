<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $L_ActDs_id
 * @property int        $L_Act_id
 * @property string     $L_ActDs_author
 * @property string     $L_ActDs_email
 * @property string     $L_ActDs_topic
 * @property string     $L_ActDs_body
 * @property string     $L_ActDs_key
 * @property int        $L_ActDs_confirm
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class LActDiscuss extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'L_Act_Discuss';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'L_ActDs_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'L_ActDs_id', 'L_Act_id', 'L_ActDs_author', 'L_ActDs_email', 'L_ActDs_topic', 'L_ActDs_body', 'L_ActDs_key', 'L_ActDs_confirm', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'L_ActDs_id' => 'int', 'L_Act_id' => 'int', 'L_ActDs_author' => 'string', 'L_ActDs_email' => 'string', 'L_ActDs_topic' => 'string', 'L_ActDs_body' => 'string', 'L_ActDs_key' => 'string', 'L_ActDs_confirm' => 'timestamp', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'L_ActDs_confirm', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    // public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
}
