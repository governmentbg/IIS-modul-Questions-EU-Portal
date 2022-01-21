<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Cts_id
 * @property string     $Cts_name
 * @property string     $Cts_path
 * @property string     $Cts_img
 * @property int        $A_ns_C_id
 * @property int        $Cts_order
 * @property int        $C_Arch_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CThemeSite extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Theme_Site';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Cts_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Cts_id', 'Cts_name', 'Cts_path', 'Cts_img', 'A_ns_C_id', 'Cts_order', 'Cts_date', 'C_Arch_id', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'Cts_id' => 'int', 'Cts_name' => 'string', 'Cts_path' => 'string', 'Cts_img' => 'string', 'A_ns_C_id' => 'int', 'Cts_order' => 'int', 'Cts_date' => 'date', 'C_Arch_id' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Cts_date', 'created_at', 'updated_at', 'deleted_at'
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

    public function eq_theme()
    {
        return $this->hasMany(CThemeSiteLng::class, 'Cts_id');
    }
}
