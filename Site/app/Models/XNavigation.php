<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $XN_id
 * @property int        $XN_parent_id
 * @property string     $XN_string
 * @property int        $XN_order
 * @property string     $XN_name
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class XNavigation extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'X_Navigation';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'XN_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'XN_id', 'XN_parent_id', 'XN_string', 'XN_order', 'XN_name', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'XN_id' => 'int', 'XN_parent_id' => 'int', 'XN_string' => 'string', 'XN_order' => 'int', 'XN_name' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    public function eq_nav()
    {
        return $this->hasMany(XNavList::class, 'XN_id');
    }
}
