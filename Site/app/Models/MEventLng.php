<?php

namespace App\Models;

// use App\Models\MEvent;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $M_EventL_id
 * @property int        $M_Event_id
 * @property int        $C_Lang_id
 * @property string     $M_EventL_title
 * @property string     $M_EventL_body
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MEventLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_Event_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_EventL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_EventL_id', 'M_Event_id', 'C_Lang_id', 'M_EventL_title', 'M_EventL_body', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'M_EventL_id' => 'int', 'M_Event_id' => 'int', 'C_Lang_id' => 'int', 'M_EventL_title' => 'string', 'M_EventL_body' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_event()
    {
        return $this->belongsTo(MEvent::class, 'M_Event_id');
    }
    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
