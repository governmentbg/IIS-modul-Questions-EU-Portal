<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $MP_Ab_T_id
 * @property string     $MP_Ab_T_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MPAbsenseType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'MP_Absense_Type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'MP_Ab_T_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'MP_Ab_T_id', 'MP_Ab_T_name', 'created_at', 'updated_at', 'deleted_at'
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
        'MP_Ab_T_id' => 'int', 'MP_Ab_T_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
        return $this->hasMany(MPAbsense::class, 'MP_Ab_T_id');
    }
    public function eq_type_log()
    {
        return $this->hasMany(MPAbsenseLog::class, 'MP_Ab_T_id');
    }
}
