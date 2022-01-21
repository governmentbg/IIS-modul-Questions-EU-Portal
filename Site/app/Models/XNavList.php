<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $XNL_id
 * @property int        $XN_id
 * @property int        $XL_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class XNavList extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'X_Nav_List';
    public static $title = 'XN_name';
    public static function label()
    {
        return 'Права';
    }


    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'XNL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'XNL_id', 'XN_id', 'XL_id', 'created_at', 'updated_at', 'deleted_at'
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
        'XNL_id' => 'int', 'XN_id' => 'int', 'XL_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    public function eq_level()
    {
        return $this->belongsTo(XLevels::class, 'XL_id');
    }

    public function eq_list()
    {
        return $this->belongsTo(XNavigation::class, 'XN_id');
    }
}
