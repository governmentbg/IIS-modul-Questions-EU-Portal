<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_MPL_id
 * @property int        $A_ns_MP_id
 * @property int        $C_Lang_id
 * @property string     $A_ns_MPL_Name1
 * @property string     $A_ns_MPL_Name2
 * @property string     $A_ns_MPL_Name3
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class TSANsMpsLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'TS_A_ns_Mps_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_MPL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_MPL_id', 'A_ns_MP_id', 'C_Lang_id', 'A_ns_MPL_Name1', 'A_ns_MPL_Name2', 'A_ns_MPL_Name3', 'created_at', 'updated_at', 'deleted_at'
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
        'A_ns_MPL_id' => 'int', 'A_ns_MP_id' => 'int', 'C_Lang_id' => 'int', 'A_ns_MPL_Name1' => 'string', 'A_ns_MPL_Name2' => 'string', 'A_ns_MPL_Name3' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_mp()
    {
        return $this->belongsTo(TSANsMps::class, 'A_ns_MP_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
