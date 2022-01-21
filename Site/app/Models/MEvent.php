<?php

namespace App\Models;

// use App\Models\EventLng;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $M_Event_id
 * @property Date       $M_Event_date
 * @property string     $M_Event_name
 * @property int        $M_Event_order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MEvent extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_Event';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_Event_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_Event_id', 'M_Event_date', 'M_Event_time', 'M_Event_name', 'M_Event_order', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'M_Event_id' => 'int', 'M_Event_date' => 'date', 'M_Event_name' => 'string', 'M_Event_order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'M_Event_date', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_event_lang()
    {
        return $this->hasMany(MEventLng::class, 'M_Event_id');
    }
}
