<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_NavL_id
 * @property int        $C_Nav_id
 * @property int        $C_Lang_id
 * @property string     $C_NavL_value
 * @property string     $C_NavL_URL
 * @property int        $C_Nav_URL_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CNavigationLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Navigation_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_NavL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_Nav_id', 'C_Lang_id', 'C_NavL_value', 'C_NavL_URL', 'C_Nav_URL_id', 'C_St_id'
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
        'C_NavL_id' => 'int', 'C_Nav_id' => 'int', 'C_Lang_id' => 'int', 'C_NavL_value' => 'string', 'C_NavL_URL' => 'string', 'C_Nav_URL_id' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_nav1()
    {
        return $this->belongsTo(CNavigation::class, 'C_Nav_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
