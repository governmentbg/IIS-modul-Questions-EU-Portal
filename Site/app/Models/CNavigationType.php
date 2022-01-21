<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_NavT_id
 * @property string     $C_NavT_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CNavigationType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Navigation_Type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_NavT_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_NavT_name'
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
        'C_NavT_id' => 'int', 'C_NavT_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_type()
    {
        return $this->hasMany(CNavigation::class, 'C_NavT_id');
    }
}
