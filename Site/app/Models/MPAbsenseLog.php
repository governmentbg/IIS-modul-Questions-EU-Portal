<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $MP_Ab_Lg_id
 * @property int        $MP_Ab_T_id
 * @property string     $MP_Ab_Lg_year
 * @property string     $MP_Ab_Lg_month
 * @property string     $MP_Ab_Lg_values
 * @property int        $A_ns_C_id
 * @property int        $A_ns_MP_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MPAbsenseLog extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'MP_Absense_Log';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'MP_Ab_Lg_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'MP_Ab_Lg_id', 'MP_Ab_T_id', 'MP_Ab_Lg_year', 'MP_Ab_Lg_month', 'MP_Ab_Lg_values', 'A_ns_C_id', 'A_ns_MP_id', 'created_at', 'updated_at', 'deleted_at'
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
        'MP_Ab_Lg_id' => 'int', 'MP_Ab_T_id' => 'int', 'MP_Ab_Lg_year' => 'string', 'MP_Ab_Lg_month' => 'string', 'MP_Ab_Lg_values' => 'string', 'A_ns_C_id' => 'int', 'A_ns_MP_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->MP_Ab_Lg_status = null;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->MP_Ab_Lg_status = null;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_mp_log()
    {
        return $this->belongsTo(ANsMps::class, 'A_ns_MP_id');
    }

    public function eq_type_log()
    {
        return $this->belongsTo(MPAbsenseType::class, 'MP_Ab_T_id');
    }
    public function eq_coll()
    {
        return $this->belongsTo(ANsColl::class, 'A_ns_C_id');
    }
}
