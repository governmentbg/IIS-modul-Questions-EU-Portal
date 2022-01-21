<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Rf_rs_id
 * @property int        $Rf_ap_id
 * @property int        $Rf_rsT_id
 * @property string     $Rf_rs_name
 * @property string     $Rf_rs_date
 * @property string     $Rf_rs_url
 * @property string     $Rf_rs_file
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class RfResource extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Rf_resource';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Rf_rs_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Rf_rs_id', 'Rf_ap_id', 'Rf_rsT_id', 'Rf_rs_name', 'Rf_rs_date', 'Rf_rs_url', 'Rf_rs_file', 'created_at', 'updated_at', 'deleted_at'
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
        'Rf_rs_id' => 'int', 'Rf_ap_id' => 'int', 'Rf_rsT_id' => 'int', 'Rf_rs_name' => 'string', 'Rf_rs_date' => 'date', 'Rf_rs_url' => 'string', 'Rf_rs_file' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
        return $this->belongsTo(RfResourceType::class, 'Rf_rsT_id');
    }
    public function eq_app()
    {
        return $this->belongsTo(RfApplication::class, 'Rf_ap_id');
    }
}
