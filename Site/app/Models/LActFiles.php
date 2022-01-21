<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $ID
 * @property int        $L_Act_id
 * @property int        $VID_DOC
 * @property string     $FILENAME
 * @property string     $TIP
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class LActFiles extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'L_Act_Files';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'L_ActF_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'L_ActF_id', 'ID', 'L_Act_id', 'VID_DOC', 'FILENAME', 'TIP', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'ID' => 'int', 'L_Act_id' => 'int', 'VID_DOC' => 'int', 'FILENAME' => 'string', 'TIP' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_act()
    {
        return $this->belongsTo(LActs::class, 'Sync_ID', 'L_Act_id');
    }
}
