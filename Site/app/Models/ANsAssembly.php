<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_id
 * @property string     $A_ns_name
 * @property Date       $A_ns_start
 * @property Date       $A_ns_end
 * @property int        $sync_ID
 * @property string     $A_ns_folder
 * @property int        $C_Arch_id
 * @property int        $C_St_id
 */
class ANsAssembly extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_ns_Assembly';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_id', 'A_ns_name', 'A_ns_start', 'A_ns_end', 'sync_ID', 'A_ns_folder', 'C_Arch_id', 'C_St_id'
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
        'A_ns_id' => 'int', 'A_ns_name' => 'string', 'A_ns_start' => 'date', 'A_ns_end' => 'date', 'sync_ID' => 'int', 'A_ns_folder' => 'string', 'C_Arch_id' => 'int', 'C_St_id' => 'int'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_ns_start', 'A_ns_end'
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
    public function eq_reg_persons()
    {
        return $this->belongsTo(RPersons::class, 'A_ns_id');
    }
}
