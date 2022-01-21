<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_Nav_id
 * @property int        $C_NavT_id
 * @property int        $C_Nav_parent_id
 * @property string     $C_Nav_name
 * @property string     $C_Nav_string
 * @property int        $C_Nav_order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CNavigation extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Navigation';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_Nav_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_NavT_id', 'C_Nav_parent_id', 'C_Nav_name', 'C_Nav_string', 'C_Nav_order', 'C_St_id'
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
        'C_Nav_id' => 'int', 'C_NavT_id' => 'int', 'C_Nav_parent_id' => 'int', 'C_Nav_name' => 'string', 'C_Nav_string' => 'string', 'C_Nav_order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
        return $this->belongsTo(CNavigationType::class, 'C_NavT_id');
    }

    public function eq_nav()
    {
        return $this->hasMany(CNavigationLng::class, 'C_Nav_id');
    }
    public function eq_article()
    {
        return $this->hasMany(CArticles::class, 'C_Nav_id');
    }

    public function parent()
    {
        return $this->belongsTo(CNavigation::class, 'C_Nav_parent_id', 'C_Nav_id');
    }

    public function children()
    {
        return $this->hasMany(CNavigation::class, 'C_Nav_parent_id', 'C_Nav_id');
    }
}
