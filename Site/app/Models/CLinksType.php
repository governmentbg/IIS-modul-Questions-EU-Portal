<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_LinkT_id
 * @property int        $C_LinkT_prior
 * @property string     $C_LinkT_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CLinksType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Links_Type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_LinkT_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_LinkT_id', 'C_LinkT_prior', 'C_LinkT_name', 'created_at', 'updated_at', 'deleted_at'
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
        'C_LinkT_id' => 'int', 'C_LinkT_prior' => 'int', 'C_LinkT_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
        return $this->hasMany(CLinks::class, 'C_LinkT_id');
    }
}
