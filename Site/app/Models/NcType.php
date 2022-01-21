<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Nc_rsT_id
 * @property string     $Nc_rsT_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class NcType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Nc_type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Nc_rsT_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Nc_rsT_id', 'Nc_rsT_name', 'created_at', 'updated_at', 'deleted_at'
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
        'Nc_rsT_id' => 'int', 'Nc_rsT_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_type()
    {
        return $this->hasMany(NcResearch::class, 'Nc_rsT_id');
    }
}
