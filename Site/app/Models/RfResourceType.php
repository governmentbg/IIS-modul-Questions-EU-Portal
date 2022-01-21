<?php

namespace App\Models;

// use App\RfResource;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Rf_rsT_id
 * @property string     $Rf_rsT_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class RfResourceType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Rf_resource_type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Rf_rsT_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Rf_rsT_id', 'Rf_rsT_name', 'created_at', 'updated_at', 'deleted_at'
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
        'Rf_rsT_id' => 'int', 'Rf_rsT_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_res_type()
    {
        return $this->hasMany(RfResource::class, 'Rf_rsT_id');
    }
}
