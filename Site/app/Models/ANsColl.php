<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_C_id
 * @property int        $A_ns_C_parent_id
 * @property string     $A_ns_C_name
 * @property int        $A_ns_CT_id
 * @property Date       $A_ns_C_date_F
 * @property Date       $A_ns_C_date_T
 * @property string     $A_ns_C_note
 * @property int        $A_ns_id
 * @property int        $A_ns_C_order
 * @property int        $C_St_id
 * @property int        $sync_ID
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ANsColl extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_ns_Coll';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_C_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_C_id', 'A_ns_C_parent_id', 'A_ns_C_name', 'A_ns_CT_id', 'A_ns_C_date_F', 'A_ns_C_date_T', 'A_ns_C_note', 'A_ns_id', 'A_ns_C_order', 'C_St_id', 'sync_ID', 'created_at', 'updated_at', 'deleted_at'
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
        'A_ns_C_id' => 'int', 'A_ns_C_parent_id' => 'int', 'A_ns_C_name' => 'string', 'A_ns_CT_id' => 'int', 'A_ns_C_date_F' => 'date', 'A_ns_C_date_T' => 'date', 'A_ns_C_note' => 'string', 'A_ns_id' => 'int', 'A_ns_C_order' => 'int', 'C_St_id' => 'int', 'sync_ID' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_ns_C_date_F', 'A_ns_C_date_T', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_steno()
    {
        return $this->hasMany(ACmSteno::class, 'A_ns_C_id');
    }
    public function eq_stan()
    {
        return $this->hasMany(ACmStan::class, 'A_ns_C_id');
    }
    public function eq_sit()
    {
        return $this->hasMany(ACmSit::class, 'A_ns_C_id');
    }
}
